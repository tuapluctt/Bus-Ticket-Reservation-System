package vn.hvt.spring.BusTicketReservationSystem.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.entity.Ticket;
import vn.hvt.spring.BusTicketReservationSystem.enums.SeatStatus;
import vn.hvt.spring.BusTicketReservationSystem.repository.TicketRepository;
import vn.hvt.spring.BusTicketReservationSystem.service.TicketSevice;

import java.util.List;
import java.util.Optional;

@Service
public class TicketSeviceImpl implements TicketSevice {

    @Autowired
    TicketRepository ticketRepository;

    @Override
    public List<Ticket> getSeatsByRow(String row, int  tripId) {
        List<Ticket> tickets = ticketRepository.findByRowAndCarId(row,tripId);
        return tickets ;
    }

    @Override
    public Ticket updateStatusById(int ticketId, SeatStatus status) {
        Optional<Ticket> t = ticketRepository.findById(ticketId);
        Ticket ticket = t.get();
        ticket.setSeatStatus(status);
        Ticket ticketResult =  ticketRepository.save(ticket);
        return ticketResult;
    }

}
