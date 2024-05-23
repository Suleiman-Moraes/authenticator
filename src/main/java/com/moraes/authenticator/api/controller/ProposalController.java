package com.moraes.authenticator.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.service.interfaces.real_state.IProposalService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/proposal")
@AllArgsConstructor
public class ProposalController {

    private final IProposalService service;

    @PostMapping
    public ResponseEntity<Long> insert(@RequestBody @Valid ProposalDTO object) {
        final Long id = service.insert(object);
        return ResponseEntity.created(URI.create(String.format("/api/v1/proposal/%s", id))).body(id);
    }
}