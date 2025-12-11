package com.eclock.backend.auth.util;




import com.eclock.backend.auth.model.Permission;
import com.eclock.backend.auth.model.Role;
import com.eclock.backend.auth.model.User;
import com.eclock.backend.auth.repository.PermissionRepository;
import com.eclock.backend.auth.repository.RoleRepository;
import com.eclock.backend.auth.repository.UserRepository;
import com.eclock.backend.task.enums.TaskStatus;
import com.eclock.backend.task.model.Task;
import com.eclock.backend.task.repository.TaskRepository;
import com.eclock.backend.timesheet.model.Timesheet;
import com.eclock.backend.timesheet.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if no users exist
        if (userRepository.count() == 0) {
            seedData();
        }
    }

    private void seedData() {
        // Create basic permissions
        Permission viewProfile = createPermission("VIEW_PROFILE", "Can view own profile");
        Permission editProfile = createPermission("EDIT_PROFILE", "Can edit own profile");

        // Create USER role
        Role userRole = Role.builder()
            .name("ROLE_USER")
            .permissions(Set.of(viewProfile, editProfile))
            .build();
        roleRepository.save(userRole);

        // Create a sample user
        User sampleUser = User.builder()
            .username("arpan")
            .password(passwordEncoder.encode("password123"))
            .email("john.doe@example.com")
            .enabled(true)
            .roles(Set.of(userRole))
            .build();
        userRepository.save(sampleUser);

        // Create sample timesheets for the last 3 days
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 3; i++) {
            LocalDate date = today.minusDays(i);
            LocalDateTime baseDateTime = date.atStartOfDay();

            Timesheet timesheet = Timesheet.builder()
                .employee(sampleUser)
                .date(date)
                .clockIn(baseDateTime.plusHours(9))  // 9:00 AM
                .breakIn(baseDateTime.plusHours(12))  // 12:00 PM
                .breakOut(baseDateTime.plusHours(13))  // 1:00 PM
                .clockOut(baseDateTime.plusHours(17))  // 5:00 PM
                .comment("Regular working day")
                .build();
            timesheetRepository.save(timesheet);
        }

        // Create sample tasks
        createSampleTask(sampleUser, "Complete Project Proposal",
            "Draft and submit the project proposal for client review",
            TaskStatus.COMPLETED,
            LocalDateTime.now().minusDays(1));

        createSampleTask(sampleUser, "Weekly Report",
            "Prepare weekly progress report for team meeting",
            TaskStatus.IN_PROGRESS,
            LocalDateTime.now().plusDays(1));

        createSampleTask(sampleUser, "Client Meeting",
            "Prepare presentation for client meeting",
            TaskStatus.PENDING,
            LocalDateTime.now().plusDays(2));
    }

    private Permission createPermission(String name, String description) {
        return permissionRepository.save(Permission.builder()
            .name(name)
            .description(description)
            .build());
    }

    private void createSampleTask(User employee, String title, String description,
                                  TaskStatus status, LocalDateTime dueDate) {
        Task task = Task.builder()
            .employee(employee)
            .title(title)
            .description(description)
            .status(status)
            .dueDate(dueDate)
            .comment(status == TaskStatus.COMPLETED ? "Task completed on time" : "")
            .build();
        taskRepository.save(task);
    }
}
