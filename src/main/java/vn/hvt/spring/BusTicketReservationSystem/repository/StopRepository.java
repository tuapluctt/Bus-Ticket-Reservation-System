package vn.hvt.spring.BusTicketReservationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.hvt.spring.BusTicketReservationSystem.entity.Stop;
import vn.hvt.spring.BusTicketReservationSystem.entity.Trip;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StopRepository  extends JpaRepository<Stop,Integer> {
    // tên stop đầu tiên trong lịch trình
    @Query(value = "SELECT st1.stop_name " +
            "FROM stops st1 " +
            "JOIN  schedule_detail  sd1 ON sd1.stop_id = st1.stop_id " +
            "WHERE schedule_id = ?1 " +
            "ORDER BY stop_number ASC " +
            "LIMIT 1" , nativeQuery = true)
    String findFirstStopNameByScheduleIdMax(int scheduleId);
    // tên stop cuối trong lịch trình
    @Query(value = "SELECT st1.stop_name " +
            "FROM stops st1 " +
            "JOIN  schedule_detail  sd1 ON sd1.stop_id = st1.stop_id " +
            "WHERE schedule_id = ?1 " +
            "ORDER BY stop_number DESC " +
            "LIMIT 1" , nativeQuery = true)
    String findFirstStopNameByScheduleIdMin(int scheduleId);

}
