package vn.hvt.spring.BusTicketReservationSystem.controller.Admin;

import com.google.zxing.WriterException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.hvt.spring.BusTicketReservationSystem.DTO.BookingDTO;
import vn.hvt.spring.BusTicketReservationSystem.DTO.CheckInRequest;
import vn.hvt.spring.BusTicketReservationSystem.DTO.TripAdminDTO;
import vn.hvt.spring.BusTicketReservationSystem.DTO.resonse.ApiResponse;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.service.BookingSevice;
import vn.hvt.spring.BusTicketReservationSystem.service.TripSevice;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/driver")
@Controller
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class DriverController {
    TripSevice tripSevice;
    BookingSevice bookingSevice;

    @GetMapping
    public String driver(){
        return "admin/adminhomepage";
    }

    @GetMapping(value = "/bus")
    public String checkIn(Model model){
        List<TripAdminDTO> trips = tripSevice.searchByDate(LocalDate.now());

        model.addAttribute("trips", trips);
        return "admin/bus";
    }

    @GetMapping("/checkin/{tripCode}")
    public String showCheckinPage(@PathVariable(value = "tripCode") String tripCode, Model model) {
        log.info("Trip code: " + tripCode);
        model.addAttribute("currentTripCode", tripCode);
        return "admin/checkin";
    }


    @PostMapping("/api/checkin/{tripCode}")
    @ResponseBody
    public ApiResponse<BookingDTO> checkInTicket(@RequestBody CheckInRequest request, @PathVariable(value = "tripCode") String tripCode) throws IOException, WriterException {

            String idBooking = request.getIdBooking();

            BookingDTO booking = bookingSevice.checkInTicket(idBooking, tripCode);

            log.info(booking.getBookingCode());
            return ApiResponse.<BookingDTO>builder()
                    .message("Check in successfully")
                    .result(booking)
                    .build();
    }
}
