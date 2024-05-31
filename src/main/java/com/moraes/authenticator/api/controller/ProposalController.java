package com.moraes.authenticator.api.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.controller.interfaces.IController;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalFilterDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalListDTO;
import com.moraes.authenticator.api.service.interfaces.real_state.IProposalService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/proposal")
@AllArgsConstructor
public class ProposalController implements IController<ProposalDTO, Long> {

    private final IProposalService service;

    @PostMapping
    public ResponseEntity<Long> insert(@RequestBody @Valid ProposalDTO object) {
        final Long id = service.insert(object);
        return ResponseEntity.created(URI.create(String.format("/api/v1/proposal/%s", id))).body(id);
    }

    @GetMapping(value = "/{key}")
    @Override
    public ResponseEntity<ProposalDTO> findByKey(@PathVariable Long key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByKey'");
    }

    // TODO - Lacks integration testing
    @GetMapping
    public ResponseEntity<Page<ProposalListDTO>> findAll(ProposalFilterDTO proposalFilter) {
        return ResponseEntity.ok(service.findPageAll(proposalFilter));
    }
}
