package com.eclock.backend.timesheet.repository;



import com.eclock.backend.timesheet.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByEmployeeIdAndDateBetweenOrderByDateDesc(
        Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Timesheet> findByEmployeeIdOrderByDateDesc(Long employeeId);

    boolean existsByEmployeeIdAndDate(Long employeeId, LocalDate date);
}
