package vn.hvt.spring.BusTicketReservationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hvt.spring.BusTicketReservationSystem.entity.Car;
import vn.hvt.spring.BusTicketReservationSystem.entity.PriceList;

@Repository
public interface CarRepository extends JpaRepository<Car,String> {
}
