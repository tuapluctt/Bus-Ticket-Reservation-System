package vn.hvt.spring.BusTicketReservationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hvt.spring.BusTicketReservationSystem.entity.Route;

import java.util.List;
@Repository
public interface RouteRepository extends JpaRepository<Route,Integer> {
}
