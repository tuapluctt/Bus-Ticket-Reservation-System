package vn.hvt.spring.BusTicketReservationSystem.service;

import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.entity.Ticket;
import vn.hvt.spring.BusTicketReservationSystem.enums.SeatStatus;

import java.util.List;

@Service
public interface TicketSevice {
    List<Ticket> getSeatsByRow(String row , int tripId);

    Ticket updateStatusById(int ticketId, SeatStatus status);
}
