package com.moraes.authenticator.api.controller;

import java.net.URI;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.controller.interfaces.IController;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionAllDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionFilterDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionListDTO;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.model.menu.Question;
import com.moraes.authenticator.api.service.interfaces.menu.IQuestionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/question")
@AllArgsConstructor
public class QuestionController implements IController<QuestionDTO, Long> {

    private IQuestionService service;

    @Override
    @GetMapping(value = "/{key}")
    public ResponseEntity<QuestionDTO> findByKey(@PathVariable Long key) {
        QuestionDTO dto = service.parseObject(service.findByKeyAndCompanyKey(key), QuestionDTO.class,
                QuestionController.class);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<QuestionListDTO>> findAll(QuestionFilterDTO personFilter) {
        return ResponseEntity.ok(service.findPageAll(personFilter));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Long> insert(@RequestBody @Valid QuestionDTO object) {
        final Long id = service.insert(Mapper.parseObject(object, Question.class));
        return ResponseEntity.created(URI.create(String.format("/api/v1/question/%s", id))).body(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/all")
    public ResponseEntity<List<Long>> insertAll(@RequestBody @NotEmpty @NotNull List<@Valid QuestionAllDTO> dtos,
            @RequestParam(required = true) TypeFromEnum typeFrom) {
        final List<Long> ids = service.insertAll(dtos, typeFrom);
        return ResponseEntity.status(HttpStatus.CREATED).body(ids);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{key}")
    public ResponseEntity<Long> update(@RequestBody @Valid QuestionDTO object, @PathVariable long key) {
        service.update(object, key);
        return ResponseEntity.ok(key);
    }

    @DeleteMapping(value = "/{key}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long key) {
        service.delete(key);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
