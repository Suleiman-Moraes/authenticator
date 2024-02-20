package com.moraes.authenticator.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.model.dto.menu.MenuDTO;
import com.moraes.authenticator.api.service.interfaces.menu.IMenuService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final IMenuService service;

    @GetMapping
    public ResponseEntity<List<MenuDTO>> findAll() {
        log.debug("Start find all menus.");
        final List<MenuDTO> dtos = service.findAll();
        log.debug("End find all menus {}.", dtos);
        return ResponseEntity.ok(dtos);
    }
}
