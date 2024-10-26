package vn.hvt.spring.BusTicketReservationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.hvt.spring.BusTicketReservationSystem.entity.Route;
import vn.hvt.spring.BusTicketReservationSystem.service.RouteSevice;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    RouteSevice routeSevice;

    @RequestMapping(value = "/")
    public String showHomePage(Model model){
        List<Route> routes = routeSevice.findAll();

        model.addAttribute("routes" , routes);
        return "public/home";
    }
}
