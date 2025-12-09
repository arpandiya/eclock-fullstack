package com.eclock.backend.auth.dto;

import com.eclock.backend.auth.model.Role;
import lombok.Data;

import java.util.Set;
@Data
public class RegisterDto {

    private Long id;

    private String username;
    private String password;
    private String email;
    private final boolean enabled = true;


    private Set<Role> roles;
}
