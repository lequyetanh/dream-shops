package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.auth.LoginRequest;
import com.dailycodework.dreamshops.payload.dto.auth.RegisterRequest;
import com.dailycodework.dreamshops.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class Auth {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<BaseResultDTO> register(@Valid @RequestBody RegisterRequest req) {
        BaseResultDTO result = authService.register(req);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResultDTO> login(@Valid @RequestBody LoginRequest req) {
        BaseResultDTO result = authService.login(req);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
