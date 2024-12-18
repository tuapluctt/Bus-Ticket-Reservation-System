package vn.hvt.spring.BusTicketReservationSystem.service.ipml;

import com.google.zxing.WriterException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.DTO.BookingDTO;
import vn.hvt.spring.BusTicketReservationSystem.DTO.TripCard;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.entity.Ticket;
import vn.hvt.spring.BusTicketReservationSystem.enums.BookingStatus;
import vn.hvt.spring.BusTicketReservationSystem.enums.SeatStatus;
import vn.hvt.spring.BusTicketReservationSystem.repository.BookingReposity;
import vn.hvt.spring.BusTicketReservationSystem.repository.TicketRepository;
import vn.hvt.spring.BusTicketReservationSystem.repository.TripRepository;
import vn.hvt.spring.BusTicketReservationSystem.service.BookingSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.StopSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.TicketSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.TripSevice;
import vn.hvt.spring.BusTicketReservationSystem.util.QRCodeGenerator;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingSeviceIpml implements BookingSevice {

    @Autowired
    TripRepository tripRepository;

    @Autowired
    BookingReposity bookingReposity;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TicketSevice ticketSevice;

    @Autowired
    StopSevice stopSevice;

    @Autowired
    TripSevice tripSevice;


    @Override
    public Page<Booking> findAllByOrderByDateCreatedDesc(int pageNo) {
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);

        return bookingReposity.findAllByOrderByDateCreatedDesc(pageable);
    }

    @Override
    public Page<Booking> searchBookingsByKeyword(String keyword, int pageNo) {

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);

        return bookingReposity.searchByKeyword(keyword,pageable);
    }


    @Override
    public List<Booking> searchBookingsByKeyword( String keyword) {
        return bookingReposity.searchByKeyword(keyword);
    }

    @Override
    public List<Booking> getAll() {
        return bookingReposity.findAll();
    }

    @Override
    public List<Booking> findPaidOrBookedByEmail(String email) {
        List<BookingStatus> statuses = Arrays.asList(BookingStatus.PAID, BookingStatus.BOOKED);
        return bookingReposity.findByEmailAndStatusIn(email, statuses);
    }


    @Override
    public Booking getBookingById(int bookingId) {
        Optional<Booking> booking = bookingReposity.findById(bookingId);
        if(booking.isPresent()){
            return booking.get();
        }
        return null;
    }

    @Override
    public List<Booking> findByPhoneNumber(String phone) {
        return bookingReposity.findByPhoneNumber(phone);
    }

    @Transactional
    @Override
    public void updateStatus(Booking booking, BookingStatus bookingStatus) {
        if(bookingStatus == BookingStatus.BOOKED || bookingStatus == BookingStatus.PENDING){
            booking.getTickets().forEach( ticket -> {
                ticketSevice.updateStatusById(ticket.getId(), SeatStatus.BOOKED);
            });
        }else if(bookingStatus == BookingStatus.PAID ){
            booking.getTickets().forEach( ticket -> {
                ticketSevice.updateStatusById(ticket.getId(), SeatStatus.SOLD);
            });
        }else if(bookingStatus == BookingStatus.CANCELLED){
            booking.getTickets().forEach( ticket -> {
                ticketSevice.updateStatusById(ticket.getId(), SeatStatus.EMPTY);
            });
        }

        booking.setStatus(bookingStatus);

        bookingReposity.save(booking);
    }

    @Override
    public void updateQrCode(int bookingId, String QrCode) {
        Booking booking = bookingReposity.findById(bookingId).get();
        booking.setQrcode(QrCode);
        bookingReposity.save(booking);
    }

    @Transactional
    @Override
    public Booking saveBooking(int tripId, String phoneNumber, String fullName, String email, String[] listTicket , int departureId, int arrivalId) {

        // câpj nhật trạng thái ghế booked
        List<Ticket> tickets = new ArrayList<>();
        for (String ticketId : listTicket) {
            Ticket ticket = ticketRepository.findById(Integer.valueOf(ticketId)).get();
            if(ticket.getSeatStatus() == SeatStatus.EMPTY){
                Ticket ticket1 = ticketSevice.updateStatusById(Integer.valueOf(ticketId), SeatStatus.BOOKED);
                tickets.add(ticket);
            }else{

            }

        }

        // lưu ghế
        Booking booking = new Booking();
        booking.setFullName(fullName);
        booking.setEmail(email);
        booking.setPhoneNumber(phoneNumber);
        booking.setTrip(tripRepository.findById(tripId).get());
        booking.setDateCreated(LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTickets(tickets);
        booking.setDeparture(stopSevice.findById(departureId));
        booking.setArrival(stopSevice.findById(arrivalId));
        booking.setRated(false);

        return bookingReposity.save(booking);
    }


    @Override
    public BookingDTO findBookingById(int bookingId) throws IOException, WriterException {

        Booking booking = getBookingById(Integer.valueOf(bookingId));
        // sinh qr code
        String path = QRCodeGenerator.generateQRCode(booking);
        updateQrCode(booking.getId(),path);

        //gửi email
        Map<String,Object> inforBooking = new HashMap<>();
        TripCard tripCard = tripSevice.getTripCard(booking.getTrip(),booking.getDeparture().getId(),booking.getArrival().getId());


        // danh ghế của booling
        StringJoiner listSeat = new StringJoiner(",");

        booking.getTickets().forEach(ticket -> {
            listSeat.add(ticket.getSeatName());
        });

        BookingDTO bookingDTO = BookingDTO.builder()
                .bookingId(booking.getId())
                .customerName(booking.getFullName())
                .customerPhone(booking.getPhoneNumber())
                .customerEmail(booking.getEmail())
                .tripName(tripCard.getTripNamel())
                .starTime(tripCard.getStarTime())
                .departureDate(tripCard.getDepartureDate())
                .departureTime(tripCard.getDepartureTime())
                .departureLocation(tripCard.getDepartureLocation())
                .arrivalLocation(tripCard.getArrivalLocation())
                .listSeat(listSeat.toString())
                .qrCode(path)
                .build();


        return bookingDTO;
    }


}
