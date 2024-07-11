package com.moraes.authenticator.api.controller;

import com.moraes.authenticator.api.controller.interfaces.IController;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalFilterDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalListDTO;
import com.moraes.authenticator.api.service.interfaces.real_state.IProposalService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

	// TODO lacks integration tests
	@PutMapping(value = "/{key}")
	public ResponseEntity<Void> update(@RequestBody @Valid ProposalDTO object, @PathVariable long key) {
		service.update(object, key);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{key}")
	@Override
	public ResponseEntity<ProposalDTO> findByKey(@PathVariable Long key) {
		return ResponseEntity.ok(service.parse(service.findByKey(key)));
	}

	@GetMapping
	public ResponseEntity<Page<ProposalListDTO>> findAll(ProposalFilterDTO proposalFilter) {
		return ResponseEntity.ok(service.findPageAll(proposalFilter));
	}
}
