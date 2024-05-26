package vn.hvt.spring.BusTicketReservationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vn.hvt.spring.BusTicketReservationSystem.entity.Route;
import vn.hvt.spring.BusTicketReservationSystem.DTO.RouteInformation;
import vn.hvt.spring.BusTicketReservationSystem.service.RouteSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.ipml.RouteInformationSeviceipml;

@Controller
public class RouteController {
    @Autowired
    RouteSevice routeSevice;

    @Autowired
    RouteInformationSeviceipml RouteInformationSeviceipml;

    @GetMapping(value = "/public/{routeId}")
    public String route(Model model, @PathVariable(value = "routeId" ,required = true) int routeId){

        Route route = routeSevice.findById(routeId);
        RouteInformation routeInformation = RouteInformationSeviceipml.getRouteInformation(routeId);


        model.addAttribute("routeInformation", routeInformation);
        model.addAttribute("route", route);
        return "public/route";
    }
}
