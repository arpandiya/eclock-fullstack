package com.eclock.backend.timesheet.controller;


import com.eclock.backend.timesheet.dto.TimesheetDto;
import com.eclock.backend.timesheet.model.Timesheet;
import com.eclock.backend.timesheet.service.TimesheetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    @Autowired
    private TimesheetService timesheetService;

    @PostMapping
    public ResponseEntity<Timesheet> createTimesheet(
        Authentication authentication,
        @Valid @RequestBody TimesheetDto timesheetDto) {
        Timesheet timesheet = timesheetService.createTimesheet(
            authentication.getName(), timesheetDto);
        return ResponseEntity.ok(timesheet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Timesheet> updateTimesheet(
        Authentication authentication,
        @PathVariable Long id,
        @Valid @RequestBody TimesheetDto timesheetDto) {
        Timesheet timesheet = timesheetService.updateTimesheet(
            authentication.getName(), id, timesheetDto);
        return ResponseEntity.ok(timesheet);
    }

    @GetMapping
    public ResponseEntity<List<Timesheet>> getTimesheets(
        Authentication authentication,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Timesheet> timesheets = timesheetService.getEmployeeTimesheets(
            authentication.getName(), startDate, endDate);
        return ResponseEntity.ok(timesheets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimesheet(
        Authentication authentication,
        @PathVariable Long id) {
        timesheetService.deleteTimesheet(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
