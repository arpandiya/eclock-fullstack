package com.eclock.backend.auth.seed;

import com.eclock.backend.auth.model.AppRole;
import com.eclock.backend.auth.model.AppUser;
import com.eclock.backend.auth.repository.AppRoleRepository;
import com.eclock.backend.auth.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private AppRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if(userRepository.count() < 1) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password123"));
            AppRole adminRole = new AppRole();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }


    }
}
