package com.eclock.backend.auth.controller;

import com.eclock.backend.auth.dto.AuthRequest;
import com.eclock.backend.auth.dto.RegisterDto;
import com.eclock.backend.auth.model.Permission;
import com.eclock.backend.auth.model.Role;
import com.eclock.backend.auth.model.User;
import com.eclock.backend.auth.repository.PermissionRepository;
import com.eclock.backend.auth.repository.RoleRepository;
import com.eclock.backend.auth.repository.UserRepository;
import com.eclock.backend.auth.service.MyUserDetailsService;
import com.eclock.backend.auth.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true", allowedHeaders = "*")

public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .enabled(true)
            .build();

        Role roleUser = roleRepository.findByName("ROLE_USER")
            .orElseGet(() -> {
                // Create basic permissions for new user role if they don't exist
                Permission viewProfile = permissionRepository.findByName("VIEW_PROFILE")
                    .orElseGet(() -> permissionRepository.save(
                        Permission.builder()
                            .name("VIEW_PROFILE")
                            .description("Can view own profile")
                            .build()
                    ));
                Permission editProfile = permissionRepository.findByName("EDIT_PROFILE")
                    .orElseGet(() -> permissionRepository.save(
                        Permission.builder()
                            .name("EDIT_PROFILE")
                            .description("Can edit own profile")
                            .build()
                    ));

                // Create and save the user role with basic permissions
                return roleRepository.save(Role.builder()
                    .name("ROLE_USER")
                    .permissions(Set.of(viewProfile, editProfile))
                    .build());
            });

        user.setRoles(Set.of(roleUser));
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        Cookie cookie = new Cookie("Authorization", "Bearer " + jwt);
        cookie.setHttpOnly(true);        // Critical: JS cannot read it
        cookie.setSecure(true);           // MUST be true in production (HTTPS only)
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);   // 1 day
        cookie.setAttribute("SameSite", "Strict"); // or "Lax" if you need cross-site

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());


        return ResponseEntity.ok()
            .headers(headers)
            .body(Map.of("message", "Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete cookie
        cookie.setAttribute("SameSite", "Strict");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
            .headers(headers)
            .body(Map.of("message", "Logged out"));
    }

    @GetMapping("/authenticated-user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        assert user != null;
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("roles", user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }


//    @PostMapping("/refresh")
//    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
//        return refreshTokenService.refreshAccessToken(request.refreshToken())
//            .map(newTokens -> ResponseEntity.ok(newTokens))
//            .orElseThrow(() -> new AuthException("Invalid refresh token", HttpStatus.UNAUTHORIZED));
//    }
}
