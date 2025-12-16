package com.eclock.backend.auth.records;

import com.eclock.backend.auth.model.AppRole;

import java.util.Set;

public record RegisterRequest(String username, String password, String email, Set<AppRole> roles) {
}
