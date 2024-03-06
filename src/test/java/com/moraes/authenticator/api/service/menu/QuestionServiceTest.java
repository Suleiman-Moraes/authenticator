package com.moraes.authenticator.api.service.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.mock.menu.MockQuestion;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.menu.Question;
import com.moraes.authenticator.api.repository.IQuestionRepository;
import com.moraes.authenticator.api.service.interfaces.IUserService;

class QuestionServiceTest {

    private static final Long KEY = 1L;

    private MockQuestion input;

    @Spy
    @InjectMocks
    private QuestionService service;

    @Mock
    private IQuestionRepository repository;

    @Mock
    private IUserService userService;

    private Question entity;

    @BeforeEach
    void setUp() {
        input = new MockQuestion();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(KEY);
    }

    @Test
    void testFindByKey() {
        when(repository.findById(KEY)).thenReturn(Optional.of(entity));
        final Question ret = service.findByKey(KEY);
        assertNotNull(ret, "Return null");
        assertEquals(entity, ret, "Entity not equal");
    }

    @Test
    @DisplayName("Junit Test Given Question When Insert Then Return Question Key")
    void testGivenQuestionWhenInsertThenReturnQuestionKey() {
        mockUserServiceGetMe();

        final Long key = service.insert(entity);

        assertEquals(KEY, key, "Key not equal");
        verify(service, times(0)).getNextOrder(anyLong(), any());
    }

    @Test
    @DisplayName("Junit Test Given Question and null order When Insert Then Return Question Key")
    void testGivenQuestionAndNullOrderWhenInsertThenReturnQuestionKey() {
        mockUserServiceGetMe();
        entity.setOrder(null);

        final Long key = service.insert(entity);

        assertEquals(KEY, key, "Key not equal");
        verify(service, times(1)).getNextOrder(anyLong(), any());
    }

    private void mockUserServiceGetMe() {
        User user = new MockUser().mockEntity(1);
        user.getCompany().setKey(KEY);
        when(userService.getMe()).thenReturn(user);
    }
}
