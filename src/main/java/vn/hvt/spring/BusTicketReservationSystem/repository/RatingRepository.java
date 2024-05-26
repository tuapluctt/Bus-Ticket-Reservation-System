package vn.hvt.spring.BusTicketReservationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.entity.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Integer> {
}
