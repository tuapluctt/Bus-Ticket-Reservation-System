package vn.hvt.spring.BusTicketReservationSystem.service;

import com.google.zxing.WriterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.DTO.BookingDTO;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.enums.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface BookingSevice {
    Page<Booking> findAllByOrderByDateCreatedDesc(int pageNo);
    Page<Booking> searchBookingsByKeyword(String keyword,int pageNo);
    List<Booking> searchBookingsByKeyword(String keyword);
    public List<Booking> getAll();

    List<Booking> findPaidOrBookedByEmail(String phoneNumber);
    public Booking getBookingById(int bookingId);

    public List<Booking> findByPhoneNumber(String phone);
    public void updateStatus(Booking booking, BookingStatus bookingStatus);

    public void updateQrCode(int bookingId, String QrCode);
    public Booking saveBooking(int tripId, String phoneNumber, String fullName, String email, String[] listTicket,int departureId,int arrivalId) throws IOException, WriterException;

    BookingDTO findBookingById(int id) throws IOException, WriterException;
}
