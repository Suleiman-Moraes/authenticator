package com.moraes.authenticator.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.model.dto.user.UserNewPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordTokenDTO;
import com.moraes.authenticator.api.service.interfaces.IBasicTokenService;
import com.moraes.authenticator.api.service.interfaces.IUserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/user/me")
@AllArgsConstructor
public class UserMeController {

    private IUserService service;

    private IBasicTokenService basicTokenService;

    @PatchMapping(value = "disabled")
    public ResponseEntity<Void> updateDisabledMe() {
        service.updateDisabledMe();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "password")
    public ResponseEntity<Void> changePasswordMe(@RequestBody @Valid UserNewPasswordDTO userNewPasswordDTO) {
        service.changePasswordMe(userNewPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid UserResetPasswordDTO userResetPasswordDTO) {
        basicTokenService.validateBasicToken();
        service.resetPassword(userResetPasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "password/reset/token")
    public ResponseEntity<Void> resetPasswordToken(
            @RequestBody @Valid UserResetPasswordTokenDTO userResetPasswordTokenDTO) {
        basicTokenService.validateBasicToken();
        service.resetPassword(userResetPasswordTokenDTO);
        return ResponseEntity.noContent().build();
    }
}
