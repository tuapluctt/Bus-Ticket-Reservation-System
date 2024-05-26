package vn.hvt.spring.BusTicketReservationSystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hvt.spring.BusTicketReservationSystem.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip,Integer> {
    // tìm kiếm chuyến đi theo lịch trình và ngày và thời gian hiện tại lúc tìm kiếm phải trước 30p khi xe khởi hành
    @Query( value = "SELECT t.* , sd_max.timee " +
            "FROM trips t " +
            "JOIN schedule s ON t.schedule_id = s.schedule_id " +
            "JOIN ( " +
            "   SELECT schedule_id,timee, MIN(stop_number) AS max_number " +
            "FROM schedule_detail " +
            "WHERE schedule_id = ?1 " +
            ") sd_max ON s.schedule_id = sd_max.schedule_id " +
            "WHERE s.schedule_id = ?1 " +
            "AND DATE(t.start_date) = ?2 " +
            "AND  NOW() < DATE_SUB(STR_TO_DATE(CONCAT( ?2 , ' ', sd_max.timee), '%Y-%m-%d %H:%i:%s'), INTERVAL 30 MINUTE) ;"
            , nativeQuery = true)
    List<Trip> findTripsByScheduleAndDepartureDateTimeGreaterThan(int scheduleId, LocalDate startDate);

    // Tìm các chuyến đi bắt đầu vào một ngày cụ thể

    @Query("SELECT t FROM Trip t " +
            "WHERE t.startDate = :startDate ")
    List<Trip> findByStartDate(@Param("startDate") LocalDate startDate);


    @Query("SELECT t FROM Trip t " +
            "JOIN Schedule AS s ON t.schedule.id = s.id " +
            "WHERE s.route.id = :routeId ")
    // Tìm các chuyến đi theo route
    List<Trip> findByRoute(@Param("routeId") int route);

    @Query("SELECT t FROM Trip t " +
            "JOIN Schedule AS s ON t.schedule.id = s.id " +
            "WHERE s.route.id = :routeId AND t.startDate = :startDate")
    // Tìm các chuyến đi bắt đầu vào một ngày cụ thể và theo route
    List<Trip> findByStartDateAndRoute(@Param("startDate") LocalDate startDate,@Param("routeId") int route);

}
