package vn.hvt.spring.BusTicketReservationSystem.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.entity.Route;
import vn.hvt.spring.BusTicketReservationSystem.repository.RouteRepository;
import vn.hvt.spring.BusTicketReservationSystem.service.RouteSevice;

import java.util.List;
import java.util.Optional;

@Service
public class RouteSeviceIpml implements RouteSevice {
    @Autowired
    RouteRepository routeRepository;


    @Override
    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    @Override
    public Route findById(int id) {
        return routeRepository.findById(id).get();
    }
}
