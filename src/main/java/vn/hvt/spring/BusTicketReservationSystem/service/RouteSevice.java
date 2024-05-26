package vn.hvt.spring.BusTicketReservationSystem.service;

import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.entity.Route;

import java.util.List;
import java.util.Optional;

@Service
public interface RouteSevice {
    public List<Route> findAll();
    public Route findById(int id);
}
