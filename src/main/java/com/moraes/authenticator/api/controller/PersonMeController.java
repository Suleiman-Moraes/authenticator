package com.moraes.authenticator.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonMeDTO;
import com.moraes.authenticator.api.service.interfaces.IBasicTokenService;
import com.moraes.authenticator.api.service.interfaces.IPersonService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/person/me")
@AllArgsConstructor
public class PersonMeController {

    private final IPersonService service;

    private final IBasicTokenService basicTokenService;

    @GetMapping
    public ResponseEntity<PersonDTO> getMe() {
        PersonDTO dto = Mapper.parseObject(service.getMe(), PersonDTO.class);
        dto.getUser().setPassword(null);
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Long> insertMe(@RequestBody @Valid PersonMeDTO object) {
        basicTokenService.validateBasicToken();
        final Long id = service.insertMe(Mapper.parseObject(object, Person.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping
    public ResponseEntity<Long> updateMe(@RequestBody @Valid PersonDTO object) {
        final Long id = service.updateMe(object);
        return ResponseEntity.ok(id);
    }
}
