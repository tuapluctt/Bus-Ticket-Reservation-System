package vn.hvt.spring.BusTicketReservationSystem.service.ipml;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.entity.Rating;
import vn.hvt.spring.BusTicketReservationSystem.entity.Trip;
import vn.hvt.spring.BusTicketReservationSystem.entity.User;
import vn.hvt.spring.BusTicketReservationSystem.repository.RatingRepository;
import vn.hvt.spring.BusTicketReservationSystem.service.BookingSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.RatingSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.TripSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.UserSevice;

@Service
public class RatingSeviceIpml implements RatingSevice {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    UserSevice userSevice;
    @Autowired
    TripSevice tripSevice;
    @Autowired
    BookingSevice bookingSevice;

    @Transactional
    @Override
    public void saveRating(String phoneNumber, int bookingId, byte point, String describe) {

        User user = userSevice.findByEmail(phoneNumber);
        Booking booking = bookingSevice.getBookingById(bookingId);

        booking.setRated(true);
        Rating rating = new Rating();
        rating.setRatingNumber(point);
        rating.setBooking(booking);
        rating.setUser(user);
        rating.setDescribe(describe);

        ratingRepository.save(rating);

    }

    @Override
    public boolean checkRating(int bookingId) {
        Booking booking = bookingSevice.getBookingById(bookingId);
        return booking.isRated();
    }
}
