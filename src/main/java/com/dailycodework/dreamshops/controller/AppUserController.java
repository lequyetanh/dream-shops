package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.user.PasswordChangeReq;
import com.dailycodework.dreamshops.payload.dto.user.UserInfo;
import com.dailycodework.dreamshops.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserController {
    private final IUserService userService;

    @GetMapping("/user/get-by-company/{companyId}")
    public ResponseEntity<BaseResultDTO> getUsersByCompany(@PathVariable Long companyId) {
        return new ResponseEntity<>(userService.getUsersByCompany(companyId), HttpStatus.OK);
    }

    @GetMapping("/user/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/user/create")
    public ResponseEntity<BaseResultDTO> createUser(@RequestBody UserInfo userInfo) {
        return new ResponseEntity<>(userService.createUser(userInfo), HttpStatus.OK);
    }

    @PostMapping("/user/update")
    public ResponseEntity<BaseResultDTO> updateUser(@RequestBody UserInfo userInfo) {
        return new ResponseEntity<>(userService.updateUser(userInfo), HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @PostMapping("/user/change-password")
    public ResponseEntity<BaseResultDTO> changePassword(@RequestBody PasswordChangeReq req) {
        return new ResponseEntity<>(userService.changePassword(req), HttpStatus.OK);
    }

    @PostMapping("/user/toggle-active/{id}")
    public ResponseEntity<BaseResultDTO> toggleActive(@PathVariable Long id) {
        return new ResponseEntity<>(userService.toggleActive(id), HttpStatus.OK);
    }
}
