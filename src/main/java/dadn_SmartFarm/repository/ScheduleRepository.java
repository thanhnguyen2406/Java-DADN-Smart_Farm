package dadn_SmartFarm.repository;

import dadn_SmartFarm.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s " +
            "from Schedule s " +
            "WHERE s.device.id = :deviceId " +
            "AND FUNCTION('MONTH', s.startDate) = :month " +
            "AND FUNCTION('YEAR', s.startDate) = :year")
    List<Schedule> findSchedulesByDeviceAndMonthYear(@Param("deviceId") long deviceId, @Param("month") int month, @Param("year") int year);

    // Lấy các schedule có startDate cụ thể rơi vào tháng và năm
    @Query("SELECT s FROM Schedule s " +
            "WHERE s.device.id = :deviceId " +
            "AND s.startDate IS NOT NULL " +
            "AND FUNCTION('MONTH', s.startDate) = :month " +
            "AND FUNCTION('YEAR', s.startDate) = :year")
    List<Schedule> findSchedulesWithStartDateInMonth(
            @Param("deviceId") long deviceId,
            @Param("month") int month,
            @Param("year") int year
    );

    // Lấy các schedule có kiểu DAILY hoặc WEEKLY (lặp lại)
    @Query("SELECT s FROM Schedule s " +
            "WHERE s.device.id = :deviceId " +
            "AND (s.scheduleType = 'DAILY' OR s.scheduleType = 'WEEKLY')")
    List<Schedule> findRecurringSchedules(@Param("deviceId") long deviceId);

    List<Schedule> findByDeviceId(long deviceId);

    @Query("SELECT s FROM Schedule s WHERE " +
            "(s.scheduleType = 'ONCE' AND s.startDate <= :endDate AND (s.endDate IS NULL OR s.endDate >= :startDate)) OR " +
            "s.scheduleType = 'DAILY' OR " +
            "s.scheduleType = 'WEEKLY'")
    List<Schedule> findSchedulesForPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}