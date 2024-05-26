package vn.hvt.spring.BusTicketReservationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hvt.spring.BusTicketReservationSystem.entity.Booking;
import vn.hvt.spring.BusTicketReservationSystem.entity.Route;
import vn.hvt.spring.BusTicketReservationSystem.entity.Schedule;
import vn.hvt.spring.BusTicketReservationSystem.entity.ScheduleDetail;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScheduleDetailRepository extends JpaRepository<ScheduleDetail,Integer> {

    @Query("SELECT sd FROM ScheduleDetail sd WHERE sd.schedule.id = :scheduleId AND sd.stop.tinhtp.id = :tinhtpId ORDER BY sd.stopNumber ASC")
    List<ScheduleDetail> findScheduleDetailsByScheduleAndTinhtp(@Param("scheduleId") int scheduleId, @Param("tinhtpId") int tinhtpId);



    @Query("SELECT sd FROM ScheduleDetail sd WHERE sd.schedule.id = :scheduleId " +
            "AND sd.stop.id = :stopId ")
    ScheduleDetail findScheduleDetailByScheduleAndStop(@Param("scheduleId") int scheduleId, @Param("stopId") int stopId);

    @Query(value = "SELECT sd.* " +
            "FROM schedule_detail sd " +
            "WHERE sd.schedule_id = ?1 " +
            "ORDER BY stop_number desc " +
            "LIMIT 1" ,nativeQuery = true)
    ScheduleDetail getScheduleDetailStart(int scheduleId);

    List<ScheduleDetail> findByScheduleIdIn(List<Integer> scheduleIds);
}
