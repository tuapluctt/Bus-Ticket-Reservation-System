package vn.hvt.spring.BusTicketReservationSystem.service;

import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.DTO.RouteInformation;

@Service
public interface RouteInformationSevice {
    public RouteInformation getRouteInformation(int routeId);
}
