package com.moraes.authenticator.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.model.dto.user.UserEnabledDTO;
import com.moraes.authenticator.api.service.interfaces.IUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private IUserService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping(value = "enabled/{key}")
    public ResponseEntity<Void> updateEnabled(@PathVariable long key, @RequestBody @Valid UserEnabledDTO entity) {
        service.updateEnabled(entity, key);
        return ResponseEntity.noContent().build();
    }
}
