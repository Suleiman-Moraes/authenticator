package com.moraes.authenticator.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.service.interfaces.IUserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/user/me")
@AllArgsConstructor
public class UserMeController {

    private IUserService service;

    @PatchMapping(value = "enabled")
    public ResponseEntity<Void> updateEnabledMe() {
        service.updateEnabledMe();
        return ResponseEntity.noContent().build();
    }
}
