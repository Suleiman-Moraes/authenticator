package com.moraes.authenticator.api.service.interfaces.menu;

import java.util.List;

import com.moraes.authenticator.api.model.dto.menu.question.QuestionAllDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.model.menu.Question;
import com.moraes.authenticator.api.service.interfaces.IService;

public interface IQuestionService extends IService<Question, Long> {

    List<Long> insertAll(List<QuestionAllDTO> dtos, TypeFromEnum typeFrom);

    Long update(QuestionDTO dto, Long key);
}
