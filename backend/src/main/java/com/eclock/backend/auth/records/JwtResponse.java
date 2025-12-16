package com.eclock.backend.auth.records;

import java.util.List;

public record JwtResponse(String token,  String username, List<String> roles, List<String> permissions, String type) {
    public JwtResponse {
        type = "Bearer";
    }
}
