package com.eclock.backend.timesheet.service;


import com.eclock.backend.auth.model.User;
import com.eclock.backend.auth.repository.UserRepository;
import com.eclock.backend.timesheet.dto.TimesheetDto;
import com.eclock.backend.timesheet.model.Timesheet;
import com.eclock.backend.timesheet.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private UserRepository userRepository;

    public Timesheet createTimesheet(String username, TimesheetDto timesheetDto) {
        User employee = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (timesheetRepository.existsByEmployeeIdAndDate(employee.getId(), timesheetDto.getDate())) {
            throw new IllegalStateException("Timesheet already exists for this date");
        }

        Timesheet timesheet = Timesheet.builder()
            .employee(employee)
            .date(timesheetDto.getDate())
            .clockIn(timesheetDto.getClockIn())
            .clockOut(timesheetDto.getClockOut())
            .breakIn(timesheetDto.getBreakIn())
            .breakOut(timesheetDto.getBreakOut())
            .comment(timesheetDto.getComment())
            .build();

        return timesheetRepository.save(timesheet);
    }

    public Timesheet updateTimesheet(String username, Long timesheetId, TimesheetDto timesheetDto) {
        Timesheet timesheet = timesheetRepository.findById(timesheetId)
            .orElseThrow(() -> new EntityNotFoundException("Timesheet not found"));

        if (!timesheet.getEmployee().getUsername().equals(username)) {
            throw new IllegalStateException("Not authorized to update this timesheet");
        }

        timesheet.setClockIn(timesheetDto.getClockIn());
        timesheet.setClockOut(timesheetDto.getClockOut());
        timesheet.setBreakIn(timesheetDto.getBreakIn());
        timesheet.setBreakOut(timesheetDto.getBreakOut());
        timesheet.setComment(timesheetDto.getComment());

        return timesheetRepository.save(timesheet);
    }

    public List<Timesheet> getEmployeeTimesheets(String username, LocalDate startDate, LocalDate endDate) {
        User employee = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (startDate != null && endDate != null) {
            return timesheetRepository.findByEmployeeIdAndDateBetweenOrderByDateDesc(
                employee.getId(), startDate, endDate);
        }

        return timesheetRepository.findByEmployeeIdOrderByDateDesc(employee.getId());
    }

    public void deleteTimesheet(String username, Long timesheetId) {
        Timesheet timesheet = timesheetRepository.findById(timesheetId)
            .orElseThrow(() -> new EntityNotFoundException("Timesheet not found"));

        if (!timesheet.getEmployee().getUsername().equals(username)) {
            throw new IllegalStateException("Not authorized to delete this timesheet");
        }

        timesheetRepository.delete(timesheet);
    }
}
