package com.moraes.authenticator.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.controller.interfaces.IController;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.menu.Question;
import com.moraes.authenticator.api.service.interfaces.menu.IQuestionService;

import jakarta.validation.Valid;
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
    @PostMapping
    public ResponseEntity<Long> insert(@RequestBody @Valid QuestionDTO object) {
        final Long id = service.insert(Mapper.parseObject(object, Question.class));
        return ResponseEntity.created(URI.create(String.format("/api/v1/question/%s", id))).body(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/{key}")
    public ResponseEntity<Long> update(@RequestBody @Valid QuestionDTO object, @PathVariable long key) {
        service.update(object, key);
        return ResponseEntity.ok(key);
    }
}
