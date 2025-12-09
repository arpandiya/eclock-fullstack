package com.eclock.backend.task.dto;



import com.eclock.backend.task.enums.TaskStatus;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStatusUpdateDto {
    @NotNull(message = "Status is required")
    private TaskStatus status;
    private String comment;
}
