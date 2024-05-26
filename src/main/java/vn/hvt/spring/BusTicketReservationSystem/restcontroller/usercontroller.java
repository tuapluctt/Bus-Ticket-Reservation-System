package vn.hvt.spring.BusTicketReservationSystem.restcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hvt.spring.BusTicketReservationSystem.service.ipml.UserSeviceIpml;

@RestController
@RequestMapping(value = "/api/user")
public class usercontroller {

    private UserSeviceIpml userSeviceIpml;

//    @PostMapping
//    public RegisterUser createUser(@RequestBody RegisterUser registerUser){
//
//    }
}
