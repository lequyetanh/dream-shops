package com.dailycodework.dreamshops.payload.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private String username;
    private String fullName;
    private String role;
    private Long companyId;

    public AuthResponse(String token, String username, String fullName, String role, Long companyId) {
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.companyId = companyId;
    }
}
