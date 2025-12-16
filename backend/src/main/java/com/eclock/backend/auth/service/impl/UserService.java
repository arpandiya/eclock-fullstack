package com.eclock.backend.auth.service.impl;

import com.eclock.backend.auth.model.AppRole;
import com.eclock.backend.auth.model.AppUser;
import com.eclock.backend.auth.records.RegisterRequest;
import com.eclock.backend.auth.repository.AppRoleRepository;
import com.eclock.backend.auth.repository.AppUserRepository;
import com.eclock.backend.auth.service.IUserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@NoArgsConstructor
@Getter
@Setter
public class UserService implements IUserService {


    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppRoleRepository appRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository appUserRepository,
                       AppRoleRepository appRoleRepository,
                       PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(RegisterRequest registerRequest) {

        // 1. Check if the user already exists (recommended step)
        if (appUserRepository.findByUsername(registerRequest.username()).isPresent()) {
            // You might throw a custom exception here instead of returning false
            return false;
        }

        // 2. Create the User Entity
        AppUser user = new AppUser();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        // Hash the password
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        // 3. Handle Roles (CRITICAL FIX)
        Set<AppRole> assignedRoles = new HashSet<>();

        // Iterate over the roles provided in the request
        for (AppRole roleRequest : registerRequest.roles()) {

            // Find the existing Role entity in the database by its name
            Optional<AppRole> existingRole = appRoleRepository.findByName(roleRequest.getName());

            if (existingRole.isPresent()) {
                // Add the persistent Role entity to the set
                assignedRoles.add(existingRole.get());
            } else {
                // OPTIONAL: If a role doesn't exist, handle it (e.g., throw an exception
                // or create a default role if allowed)
                System.err.println("Role not found: " + roleRequest.getName());
                // You should probably throw an exception here:
                // throw new RoleNotFoundException("Invalid role provided: " + roleRequest.getName());
            }
        }

        // Assign the retrieved roles to the user
        user.setRoles(assignedRoles);

        // 4. Save the User
        // Note: Since roles are retrieved from the DB, you do not save the role entity itself here.
        appUserRepository.save(user);

        return true;
    }
}
