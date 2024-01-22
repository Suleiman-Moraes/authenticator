package com.moraes.authenticator.api.controller;

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
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileFilterDTO;
import com.moraes.authenticator.api.service.interfaces.IProfileService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
public class ProfileController implements IController<ProfileDTO, Long> {

    private IProfileService service;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROOT')")
    @GetMapping(value = "/{key}")
    public ResponseEntity<ProfileDTO> findByKey(@PathVariable Long key) {
        ProfileDTO dto = service.parseObject(service.findByKey(key), ProfileDTO.class, ProfileController.class);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAuthority('ROOT')")
    @PostMapping
    public ResponseEntity<Long> insert(@RequestBody @Valid ProfileDTO object) {
        final Long id = service.insert(Mapper.parseObject(object, Profile.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PreAuthorize("hasAuthority('ROOT')")
    @PutMapping(value = "/{key}")
    public ResponseEntity<Long> update(@RequestBody @Valid ProfileDTO object, @PathVariable long key) {
        service.update(object, key);
        return ResponseEntity.ok(key);
    }

    @SuppressWarnings("rawtypes")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ROOT')")
    @GetMapping
    public ResponseEntity<Page<KeyDescriptionDTO>> findAll(ProfileFilterDTO filter) {
        final Page<KeyDescriptionDTO> page = service.findPageAll(filter);
        return ResponseEntity.ok(page);
    }

    @PreAuthorize("hasAuthority('ROOT')")
    @DeleteMapping(value = "/{key}")
    public ResponseEntity<Void> delete(@PathVariable Long key) {
        service.delete(key);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
