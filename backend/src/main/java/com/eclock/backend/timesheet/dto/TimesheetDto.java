package com.eclock.backend.timesheet.dto;



import lombok.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetDto {
    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
    private LocalDateTime breakIn;
    private LocalDateTime breakOut;
    private String comment;
}
