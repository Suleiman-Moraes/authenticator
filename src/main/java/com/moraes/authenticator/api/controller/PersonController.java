package com.moraes.authenticator.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.PersonDTO;
import com.moraes.authenticator.api.service.interfaces.IPersonService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/person")
@AllArgsConstructor
public class PersonController {

    private IPersonService service;
}
