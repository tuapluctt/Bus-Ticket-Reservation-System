package vn.hvt.spring.BusTicketReservationSystem.service;

import org.springframework.stereotype.Service;

@Service
public interface RatingSevice {
    void saveRating(String phoneNumber, int booking, byte point, String describe);
    boolean checkRating(int bookingId);
}
