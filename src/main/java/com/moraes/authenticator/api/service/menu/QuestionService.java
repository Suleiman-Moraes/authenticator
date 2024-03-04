package com.moraes.authenticator.api.service.menu;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.repository.IQuestionRepository;
import com.moraes.authenticator.api.service.interfaces.menu.IQuestionService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class QuestionService implements IQuestionService {

    @Getter
    private IQuestionRepository repository;
}
