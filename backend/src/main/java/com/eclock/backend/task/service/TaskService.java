package com.eclock.backend.task.service;


import com.eclock.backend.auth.model.AppUser;
import com.eclock.backend.auth.repository.AppUserRepository;
import com.eclock.backend.task.dto.TaskDto;
import com.eclock.backend.task.dto.TaskStatusUpdateDto;
import com.eclock.backend.task.enums.TaskStatus;
import com.eclock.backend.task.model.Task;
import com.eclock.backend.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AppUserRepository userRepository;

    public Task createTask(String username, TaskDto taskDto) {
        AppUser employee = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Task task = Task.builder()
            .employee(employee)
            .title(taskDto.getTitle())
            .description(taskDto.getDescription())
            .status(TaskStatus.PENDING)
            .comment(taskDto.getComment())
            .dueDate(taskDto.getDueDate())
            .build();

        return taskRepository.save(task);
    }

    public Task updateTask(String username, Long taskId, TaskDto taskDto) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getEmployee().getUsername().equals(username)) {
            throw new IllegalStateException("Not authorized to update this task");
        }

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setComment(taskDto.getComment());

        return taskRepository.save(task);
    }

    public Task updateTaskStatus(String username, Long taskId, TaskStatusUpdateDto statusUpdate) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getEmployee().getUsername().equals(username)) {
            throw new IllegalStateException("Not authorized to update this task");
        }

        task.setStatus(statusUpdate.getStatus());
        if (statusUpdate.getComment() != null) {
            task.setComment(statusUpdate.getComment());
        }

        return taskRepository.save(task);
    }

    public List<Task> getEmployeeTasks(String username, TaskStatus status) {
        AppUser employee = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (status != null) {
            return taskRepository.findByEmployeeIdAndStatusOrderByCreatedAtDesc(
                employee.getId(), status);
        }

        return taskRepository.findByEmployeeIdOrderByCreatedAtDesc(employee.getId());
    }

    public Task getTask(String username, Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getEmployee().getUsername().equals(username)) {
            throw new IllegalStateException("Not authorized to view this task");
        }

        return task;
    }

    public void deleteTask(String username, Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getEmployee().getUsername().equals(username)) {
            throw new IllegalStateException("Not authorized to delete this task");
        }

        taskRepository.delete(task);
    }
}
