package com.dailycodework.dreamshops.payload.dto.user;

import lombok.Data;

@Data
public class PasswordChangeReq {
    private Long userId;
    private String oldPassword;
    private String newPassword;
}
