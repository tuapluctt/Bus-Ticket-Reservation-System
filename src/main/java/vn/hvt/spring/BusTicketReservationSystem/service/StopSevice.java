package vn.hvt.spring.BusTicketReservationSystem.service;

import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.entity.Stop;
import vn.hvt.spring.BusTicketReservationSystem.entity.Tinhtp;

import java.util.List;

@Service
public interface StopSevice {
    public List<Stop> findAll();

    public Stop findById(int id);

}
