package vn.hvt.spring.BusTicketReservationSystem.controller;

import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.enums.BookingStatus;
import vn.hvt.spring.BusTicketReservationSystem.DTO.TripCard;
import vn.hvt.spring.BusTicketReservationSystem.service.*;
import vn.hvt.spring.BusTicketReservationSystem.util.QRCodeGenerator;
import vn.payos.PayOS;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Controller
public class BookingConTroller {
    private final VNPaySevice vnPaySevice;
    private final PayosService  payosService;
    private final BookingSevice bookingSevice;
    private final TicketSevice ticketSevice;
    private final TripSevice tripSevice;
    private final EmailSevice emailSevice;
    private final PayOS payOS;

    public BookingConTroller(VNPaySevice vnPaySevice, PayosService payosService, BookingSevice bookingSevice, TicketSevice ticketSevice, TripSevice tripSevice, EmailSevice emailSevice, PayOS payOS) {
        this.vnPaySevice = vnPaySevice;
        this.payosService = payosService;
        this.bookingSevice = bookingSevice;
        this.ticketSevice = ticketSevice;
        this.tripSevice = tripSevice;
        this.emailSevice = emailSevice;
        this.payOS = payOS;
    }

    @PostMapping("/public/bookSeats")
    public String bookSeats(@RequestParam (value = "seat", required = false) String[] litsTicket ,
                            @RequestParam (value ="tripid", required = true) int tripId,
                            @RequestParam (value ="phonenumber", required = true) String phoneNumber,
                            @RequestParam (value ="fullname", required = true) String fullName,
                            @RequestParam (value ="email", required = true) String email,
                            @RequestParam (value ="departureid", required = true) String departureId,
                            @RequestParam (value ="arrivalid", required = true) String arrivalId,
                            @RequestParam (value ="pay", required = true) String pay,
                            @RequestParam ("totalamount") int totalAmount,
                            HttpServletRequest request,
                            HttpServletResponse response
                          ) throws Exception {
        String productName = tripSevice.findById(tripId).getName();
        Booking booking = bookingSevice.saveBooking(tripId,phoneNumber,fullName,email,litsTicket,Integer.valueOf(departureId) ,Integer.valueOf(arrivalId));
        String payUrl = "";
        if(pay.equals("vnpay")){
            payUrl = vnPaySevice.createpayment(totalAmount,productName,booking.getId(),pay,request);
        }else if(pay.equals("payos")){
            payUrl = payosService.checkout(request,productName,totalAmount,booking.getId(),pay);
        }
        return "redirect:" + payUrl;
    }



    @GetMapping("/public/payment-return-vnpay")
    public String showResult(Model model, @RequestParam("vnp_Amount") String amount,
                             @RequestParam("vnp_OrderInfo") String bookingId,
                             @RequestParam("vnp_ResponseCode") String responseCode,
                             final HttpServletRequest request
    ) throws IOException, WriterException {
        if (responseCode.equals("00")){

            Booking booking = bookingSevice.getBookingById(Integer.valueOf(bookingId));
            if(booking != null){
                bookingSevice.updateStatus(booking,BookingStatus.PAID);
            }

            // sinh qr code
            String path = QRCodeGenerator.generateQRCode(booking);
            bookingSevice.updateQrCode(booking.getId(),path);

            //gửi email
            Map<String,Object> inforBooking = new HashMap<>();
            TripCard tripCard = tripSevice.getTripCard(booking.getTrip(),booking.getDeparture().getId(),booking.getArrival().getId());


            // danh ghế của booling
            StringJoiner listSeat = new StringJoiner(",");

            booking.getTickets().forEach(ticket -> {
                listSeat.add(ticket.getSeatName());
            });

            inforBooking.put("name",booking.getFullName());
            inforBooking.put("phoneNumber",booking.getPhoneNumber());
            inforBooking.put("listSeat",listSeat);

            inforBooking.put("tripNamel",tripCard.getTripNamel());
            inforBooking.put("starTime",tripCard.getStarTime());
            inforBooking.put("departureDate",tripCard.getDepartureDate());
            inforBooking.put("departureTime",tripCard.getDepartureTime());
            inforBooking.put("departureLocation",tripCard.getDepartureLocation());
            inforBooking.put("arrivalLocation",tripCard.getArrivalLocation());

            // gửi mail
           // emailSevice.sendEmailConfirmationOfSuccessfulTicket(booking.getEmail(),inforBooking);


            model.addAttribute("name",booking.getFullName());
            model.addAttribute("tripNamel",tripCard.getTripNamel());
            model.addAttribute("qrcode",booking.getQrcode());
            model.addAttribute("phoneNumber",booking.getPhoneNumber());
            model.addAttribute("amount",amount);
            model.addAttribute("departureDate",tripCard.getDepartureDate());
            model.addAttribute("departureTime",tripCard.getDepartureTime());
            model.addAttribute("departureLocation",tripCard.getDepartureLocation());
            model.addAttribute("arrivalLocation",tripCard.getArrivalLocation());
            model.addAttribute("listSeat",listSeat);
            return "public/result-payment";
        } else if (responseCode.equals("24")){
            model.addAttribute("error", "Giao dịch đã bị hủy");
            return "public/result-payment";
        }else{
            model.addAttribute("error", "Xảy ra lỗi trong quá trình thanh toán");
            return "public/result-payment";
        }
    }





    @GetMapping("/public/payment-return-payos")
    public String Result(Model model,
                         @RequestParam Map<String, String> params,
                         @RequestParam("bookingId") String bookingId
    ) throws Exception {
        String cancel = params.get("cancel");
        String orderId = params.get("orderCode");
        if (cancel.equals("false")){

            // cập nhật trạng thái booking
            Booking booking = bookingSevice.getBookingById(Integer.valueOf(bookingId));
            if(booking != null){
                bookingSevice.updateStatus(booking,BookingStatus.PAID);
            }

            // sinh qr code
            String path = QRCodeGenerator.generateQRCode(booking);
            bookingSevice.updateQrCode(booking.getId(),path);



            TripCard tripCard = tripSevice.getTripCard(booking.getTrip(),booking.getDeparture().getId(),booking.getArrival().getId());

            StringJoiner listSeat = new StringJoiner(",");
            booking.getTickets().forEach(ticket -> {
                listSeat.add(ticket.getSeatName());
            });

            //gửi email
            // danh ghế của booling
            Map<String,Object> inforBooking = new HashMap<>();
            inforBooking.put("name",booking.getFullName());
            inforBooking.put("phoneNumber",booking.getPhoneNumber());
            inforBooking.put("listSeat",listSeat);
            inforBooking.put("tripNamel",tripCard.getTripNamel());
            inforBooking.put("starTime",tripCard.getStarTime());
            inforBooking.put("departureDate",tripCard.getDepartureDate());
            inforBooking.put("departureTime",tripCard.getDepartureTime());
            inforBooking.put("departureLocation",tripCard.getDepartureLocation());
            inforBooking.put("arrivalLocation",tripCard.getArrivalLocation());


          // emailSevice.sendEmailConfirmationOfSuccessfulTicket(booking.getEmail(),inforBooking);


           PaymentLinkData paymentData = payOS.getPaymentLinkInformation(Long.valueOf(orderId));



            model.addAttribute("name",booking.getFullName());
            model.addAttribute("tripNamel",tripCard.getTripNamel());
            model.addAttribute("phoneNumber",booking.getPhoneNumber());
            model.addAttribute("amount",paymentData.getAmount());
            model.addAttribute("departureDate",tripCard.getDepartureDate());
            model.addAttribute("departureTime",tripCard.getDepartureTime());
            model.addAttribute("departureLocation",tripCard.getDepartureLocation());
            model.addAttribute("arrivalLocation",tripCard.getArrivalLocation());
            model.addAttribute("listSeat",listSeat);
            return "public/result-payment";
        } else {
            model.addAttribute("error", "Giao dịch đã bị hủy");
            return "public/result-payment";
        }
    }


}
