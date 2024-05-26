package vn.hvt.spring.BusTicketReservationSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.entity.User;
import vn.hvt.spring.BusTicketReservationSystem.enums.BookingStatus;
import vn.hvt.spring.BusTicketReservationSystem.service.BookingSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.RatingSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.UserSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.ipml.TripSeviceImpl;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    UserSevice userSevice;

    @Autowired
    BookingSevice bookingSevice;

    @Autowired
    TripSeviceImpl tripSevice;

    @Autowired
    RatingSevice ratingSevice;

    @GetMapping()
    public String showHomePage(Model model, Principal principal){
        String phoneNumber = principal.getName();
        User user = userSevice.findByPhoneNumber(phoneNumber);


        List<Booking> bookings = bookingSevice.findPaidOrBookedByPhoneNumber(phoneNumber);


        model.addAttribute("bookings", bookings);


        return "user/userhomepage";
    }

    @GetMapping("/info")
    public String userInfo(Model model, Principal principal) {
        String username = principal.getName();
        User user = userSevice.findByPhoneNumber(username);
        model.addAttribute("user", user);
        return "user_rating";
    }


    @PostMapping("/rating")
    public String rating(Model model,
                         Principal principal,
                         @RequestParam("bookingId")  int bookingId,
                         @RequestParam("rating")  byte point,
                         @RequestParam("describe")  String describe) {

        String phoneNumber = principal.getName();
        User user = userSevice.findByPhoneNumber(phoneNumber);

        String my_error = "đánh giá thành công";
        if(ratingSevice.checkRating(bookingId)){
            my_error ="Vé này đã được đánh giá đánh giá";
        }
        ratingSevice.saveRating(phoneNumber,bookingId,point,describe);

        List<Booking> bookings = bookingSevice.findByPhoneNumber(phoneNumber);
        model.addAttribute("bookings", bookings);
        model.addAttribute("my_error",my_error );

        return "user/userhomepage";
    }
}
