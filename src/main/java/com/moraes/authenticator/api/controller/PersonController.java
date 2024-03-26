package com.moraes.authenticator.api.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.controller.interfaces.IController;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonFilterDTO;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;
import com.moraes.authenticator.api.service.interfaces.IPersonService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/person")
@AllArgsConstructor
public class PersonController implements IController<PersonDTO, Long> {

    private IPersonService service;

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/{key}")
    public ResponseEntity<PersonDTO> findByKey(@PathVariable Long key) {
        PersonDTO dto = service.parseObject(service.findByKeyAndCompanyKey(key), PersonDTO.class,
                PersonController.class);
        dto.getUser().setPassword(null);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Long> insert(@RequestBody @Valid PersonDTO object) {
        final Long id = service.insert(Mapper.parseObject(object, Person.class));
        return ResponseEntity.created(URI.create(String.format("/api/v1/person/%s", id))).body(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{key}")
    public ResponseEntity<Long> update(@RequestBody @Valid PersonDTO object, @PathVariable long key) {
        service.update(object, key);
        return ResponseEntity.ok(key);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PersonListDTO>> findAll(PersonFilterDTO personFilter) {
        return ResponseEntity.ok(service.findPageAll(personFilter));
    }

    @DeleteMapping(value = "/{key}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long key) {
        service.delete(key);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
