package com.moraes.authenticator.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.service.interfaces.real_state.IConstructionService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/construction")
@AllArgsConstructor
public class ConstructionController {

    private final IConstructionService service;

    @GetMapping("/name")
    public ResponseEntity<List<String>> getNameAll() {
        return ResponseEntity.ok(service.getNameAll());
    }
}
