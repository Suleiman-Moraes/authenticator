package com.moraes.authenticator.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.service.interfaces.IAuxiliaryListService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/auxiliary-list")
@AllArgsConstructor
public class AuxiliaryListController {

    private IAuxiliaryListService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "role-enum")
    public ResponseEntity<List<KeyDescriptionDTO<String>>> getListRoleEnum() {
        return ResponseEntity.ok(service.getRoleEnumList());
    }
}
