package com.eclock.backend.task.repository;




import com.eclock.backend.task.enums.TaskStatus;
import com.eclock.backend.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
    List<Task> findByEmployeeIdAndStatusOrderByCreatedAtDesc(Long employeeId, TaskStatus status);
    List<Task> findByStatusOrderByCreatedAtDesc(TaskStatus status);
}
