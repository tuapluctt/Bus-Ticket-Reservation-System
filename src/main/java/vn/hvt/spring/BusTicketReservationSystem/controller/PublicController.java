package vn.hvt.spring.BusTicketReservationSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class PublicController {
    @GetMapping(value = "/public/com")
    public String confirmation(){
        return "public/confirmation";
    }

}
