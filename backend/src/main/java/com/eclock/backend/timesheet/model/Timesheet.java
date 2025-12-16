package com.eclock.backend.timesheet.model;


import com.eclock.backend.auth.model.AppUser;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "timesheets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser employee;

    @Column(nullable = false)
    private LocalDate date;

    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
    private LocalDateTime breakIn;
    private LocalDateTime breakOut;

    private Double totalBreakHours;
    private Double totalWorkHours;

    @Column(length = 500)
    private String comment;

    @PrePersist
    @PreUpdate
    private void calculateHours() {
        // Calculate break hours if both breakIn and breakOut are present
        if (breakIn != null && breakOut != null) {
            Duration breakDuration = Duration.between(breakIn, breakOut);
            totalBreakHours = breakDuration.toMinutes() / 60.0;
        }

        // Calculate total work hours if both clockIn and clockOut are present
        if (clockIn != null && clockOut != null) {
            Duration workDuration = Duration.between(clockIn, clockOut);
            double totalHours = workDuration.toMinutes() / 60.0;

            // Subtract break time if it exists
            if (totalBreakHours != null) {
                totalHours -= totalBreakHours;
            }

            totalWorkHours = totalHours;
        }
    }
}
