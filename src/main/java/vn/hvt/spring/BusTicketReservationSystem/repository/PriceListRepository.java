package vn.hvt.spring.BusTicketReservationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hvt.spring.BusTicketReservationSystem.entity.PriceList;
import vn.hvt.spring.BusTicketReservationSystem.entity.Route;

import java.util.List;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList,Integer> {
    List<PriceList> findByRoute(Route route);
}
