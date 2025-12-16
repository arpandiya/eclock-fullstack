package com.eclock.backend.auth.controller;


import com.eclock.backend.auth.model.AppUser;
import com.eclock.backend.auth.records.AccountCredentials;
import com.eclock.backend.auth.records.RegisterRequest;
import com.eclock.backend.auth.service.IUserService;
import com.eclock.backend.auth.service.impl.UserService;
import com.eclock.backend.auth.util.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthController(JwtService jwtService,
                          AuthenticationManager authenticationManager
    , UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody  RegisterRequest registerRequest) {
        log.info("Registering user: {}", registerRequest.toString());
        userService.register(registerRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));



    }










    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials){
        UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password());
        Authentication auth = authenticationManager.authenticate(creds);
        String jwts = jwtService.generateToken(auth);

        log.info("JWT: {}", jwts);
        log.info("Username: {}", credentials.username());
        log.info("Is Authenticated: {} ", SecurityContextHolder.getContext().getAuthentication().isAuthenticated());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwts)
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
                .build();

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Authentication authentication) {



        // Extract the JWT from the Authorization Header
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7).trim();
        }

        // If no valid Bearer token is provided, just return success (idempotent)
        if (jwt == null) {
            return ResponseEntity.ok("Logged out successfully (no token provided)");
        }

        try {
            // Validate the token and extract expiration (this will throw if invalid/signature wrong)
            Date expiration = jwtService.extractExpiration(jwt);
            Instant expirationInstant = expiration.toInstant();
            log.info("Logging out user: {}", authentication != null ? authentication.getName() : "unknown");

            // TODO: Blacklist the token here to truly invalidate it
            // Example (using a simple in-memory set or a service):
            // tokenBlacklistService.blacklistToken(jwt, Duration.between(Instant.now(), expirationInstant));

            // Without blacklisting, the token remains valid until natural expiration.
            // The code below only extracts the expiration but does nothing with it.
            // To make logout effective, you need to store the token (or its JTI) in a blacklist
            // (e.g., Redis, database, or in-memory cache with TTL set to remaining time)
            // and check the blacklist in your JWT filter before accepting the token.

        } catch (Exception e) {
            // Invalid token – treat as already invalid
            log.warn("Invalid token presented on logout: {}", e.getMessage());
        }

        // Clear SecurityContext (optional, but good practice)
        SecurityContextHolder.clearContext();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Logged out successfully");
    }


}
