package com.dailycodework.dreamshops.payload.dto.user;

import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Long companyId;
    private String role;
    private Boolean active;
}
