package com.moraes.authenticator.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.controller.interfaces.IController;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.service.interfaces.menu.IQuestionService;

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
}
