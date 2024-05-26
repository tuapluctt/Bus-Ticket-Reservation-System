package vn.hvt.spring.BusTicketReservationSystem.controller;

import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.enums.BookingStatus;
import vn.hvt.spring.BusTicketReservationSystem.DTO.TripCard;
import vn.hvt.spring.BusTicketReservationSystem.service.*;
import vn.hvt.spring.BusTicketReservationSystem.util.QRCodeGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Controller
public class BookingConTroller {
    @Autowired
    private VNPaySevice vnPaySevice;

    @Autowired
    BookingSevice bookingSevice;

    @Autowired
    TicketSevice ticketSevice;

    @Autowired
    TripSevice tripSevice;

    @Autowired
    EmailSevice emailSevice;


    @PostMapping("/public/bookSeats")
    public String bookSeats(@RequestParam (value = "seat", required = false) String[] litsTicket ,
                            @RequestParam (value ="tripid", required = true) int tripId,
                            @RequestParam (value ="phonenumber", required = true) String phoneNumber,
                            @RequestParam (value ="fullname", required = true) String fullName,
                            @RequestParam (value ="email", required = true) String email,
                            @RequestParam (value ="departureid", required = true) String departureId,
                            @RequestParam (value ="arrivalid", required = true) String arrivalId,
                            @RequestParam ("totalamount") long totalAmount,
                            HttpServletRequest request
                          ) throws IOException, WriterException {



        Booking booking = bookingSevice.saveBooking(tripId,phoneNumber,fullName,email,litsTicket,Integer.valueOf(departureId) ,Integer.valueOf(arrivalId));



        String vnpayUrl = vnPaySevice.createpayment(totalAmount,String.valueOf(booking.getId()),request);

        return "redirect:" + vnpayUrl;

//        if (litsTicket != null && litsTicket.length > 0) {
//            for (String ticket : litsTicket) {
//                System.out.println( ticket + " , + ");
//            }
//            return "public/test_seat1";
//        } else {
//            return "public/test_seat1";
//        }


    }

    @GetMapping("/public/vnpay-payment-result")
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
            emailSevice.sendEmailConfirmationOfSuccessfulTicket(booking.getEmail(),inforBooking);


            model.addAttribute("name",booking.getFullName());
            model.addAttribute("tripNamel",tripCard.getTripNamel());
            model.addAttribute("qrcode",booking.getQrcode());
            model.addAttribute("phoneNumber",booking.getPhoneNumber());
            model.addAttribute("starTime",tripCard.getStarTime());
            model.addAttribute("departureDate",tripCard.getDepartureDate());
            model.addAttribute("departureTime",tripCard.getDepartureTime());
            model.addAttribute("departureLocation",tripCard.getDepartureLocation());
            model.addAttribute("arrivalLocation",tripCard.getArrivalLocation());
            model.addAttribute("listSeat",listSeat);
            return "public/result-payment";
        }else{
            model.addAttribute("error", "Xảy ra lỗi trong quá trình thanh toán");
            return "public/result-payment";
        }
    }


}
