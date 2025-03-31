package vn.hvt.spring.BusTicketReservationSystem.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckInRequest {
    private String idBooking; // Hoặc ticketId, ticketCode
}