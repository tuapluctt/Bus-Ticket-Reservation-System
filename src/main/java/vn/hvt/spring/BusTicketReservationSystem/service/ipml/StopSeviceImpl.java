package vn.hvt.spring.BusTicketReservationSystem.service.ipml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.hvt.spring.BusTicketReservationSystem.entity.Stop;
import vn.hvt.spring.BusTicketReservationSystem.repository.RouteRepository;
import vn.hvt.spring.BusTicketReservationSystem.repository.StopRepository;
import vn.hvt.spring.BusTicketReservationSystem.service.StopSevice;

import java.util.List;

@Service
public class StopSeviceImpl implements StopSevice {

    @Autowired
    StopRepository stopRepository;

    @Override
    public List<Stop> findAll() {
        return stopRepository.findAll();
    }

    @Override
    public Stop findById(int id) {
        return stopRepository.findById(id).get();
    }
}
