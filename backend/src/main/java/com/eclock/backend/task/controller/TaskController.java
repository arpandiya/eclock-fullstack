package com.eclock.backend.task.controller;


import com.eclock.backend.task.dto.TaskDto;
import com.eclock.backend.task.dto.TaskStatusUpdateDto;
import com.eclock.backend.task.enums.TaskStatus;
import com.eclock.backend.task.model.Task;
import com.eclock.backend.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(
        Authentication authentication,
        @Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.createTask(authentication.getName(), taskDto);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
        Authentication authentication,
        @PathVariable Long id,
        @Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.updateTask(authentication.getName(), id, taskDto);
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
        Authentication authentication,
        @PathVariable Long id,
        @Valid @RequestBody TaskStatusUpdateDto statusUpdate) {
        Task task = taskService.updateTaskStatus(authentication.getName(), id, statusUpdate);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
        Authentication authentication,
        @RequestParam(required = false) TaskStatus status) {
        List<Task> tasks = taskService.getEmployeeTasks(authentication.getName(), status);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(
        Authentication authentication,
        @PathVariable Long id) {
        Task task = taskService.getTask(authentication.getName(), id);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
        Authentication authentication,
        @PathVariable Long id) {
        taskService.deleteTask(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
