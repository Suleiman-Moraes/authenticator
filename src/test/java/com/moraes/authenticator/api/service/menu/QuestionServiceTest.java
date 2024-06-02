package com.moraes.authenticator.api.service.menu;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.mock.menu.MockQuestion;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionAllDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionFilterDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionListDTO;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
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

    @Captor
    private ArgumentCaptor<Question> questionCaptor;

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

    @Test
    @DisplayName("Junit Test Given List of Question And TypeFromEnum When insertAll Then Return List of Question Key")
    void testGivenListOfQuestionAndTypeFromEnumWhenInsertAllThenReturnListOfQuestionKey() {
        mockUserServiceGetMe();

        final TypeFromEnum typeFromEnum = TypeFromEnum.PERSON;
        final int sizeList = 5;
        final int sizeValidList = sizeList - 1;
        List<QuestionAllDTO> dtos = input.mockQuestionAllDTOList(sizeList);
        dtos.set(1, null);

        final List<Long> keys = service.insertAll(dtos, typeFromEnum);

        assertNotNull(keys, "Return null");
        assertEquals(sizeList, keys.size(), "Size not equal");
        verify(service, times(sizeValidList)).insert(any(Question.class));

        ArgumentCaptor<Question> questionCaptor = ArgumentCaptor.forClass(Question.class);
        verify(service, times(sizeValidList)).insert(questionCaptor.capture());
        Question capturedQuestion = questionCaptor.getValue();
        assertEquals(typeFromEnum, capturedQuestion.getTypeFrom(), "TypeFromEnum not equal");
        assertEquals(sizeValidList, capturedQuestion.getOrder(), "Order not equal");
    }

    @Test
    @DisplayName("Junit Test Given Question And Key When Update Then Return Question Key")
    void testGivenQuestionAndKeyWhenUpdateThenReturnQuestionKey() {
        mockFindByKeyAndUserCompanyKey();

        final Long key = service.update(input.mockQuestionDTO(1), KEY);

        assertEquals(KEY, key, "Key not equal");
    }

    @Test
    @DisplayName("Junit Test Given Question When Valid Then Does not Throw Exception")
    void testGivenQuestionWhenValidThenDoesNotThrowException() {
        assertDoesNotThrow(() -> service.valid(entity), "Does Not Throw");
    }

    @Test
    @DisplayName("Junit Test Given Question When Valid Then Throw Exception")
    void testGivenQuestionWhenValidThenThrowException() {
        when(repository.existsByKeyNotAndCompanyKeyAndTypeFromAndValueIgnoreCase(eq(KEY), eq(null),
                any(TypeFromEnum.class), anyString())).thenReturn(true);
        when(repository.existsByKeyNotAndCompanyKeyAndTypeFromAndOrder(eq(KEY), eq(null),
                any(TypeFromEnum.class), anyInt())).thenReturn(true);

        final ValidException exception = assertThrows(ValidException.class, () -> service.valid(entity),
                "Should Throw ValidException");

        assertNotNull(exception, "Exception is null");
        assertEquals(2, exception.getErrs().size(), "Return size of errors is different");
        assertTrue(exception.getErrs().containsAll(
                List.of("question.company_typeFrom_value.duplicate", "question.company_typeFrom_order.duplicate")),
                "Return errors is different");
    }

    @Test
    @DisplayName("Junit Test Given CompanyKey and TypeFromEnum When GetNextOrder Then Return Next Order")
    void testGivenCompanyKeyAndTypeFromEnumWhenGetNextOrderThenReturnNextOrder() {
        final Long companyKey = 1L;
        final TypeFromEnum typeFrom = TypeFromEnum.PERSON;

        final int nextOrder = service.getNextOrder(companyKey, typeFrom);

        assertEquals(1, nextOrder, "NextOrder not equal");
    }

    @Test
    @DisplayName("Junit Test Given CompanyKey and TypeFromEnum When GetNextOrder Then Return Next Order Greater Than 1")
    void testGivenCompanyKeyAndTypeFromEnumWhenGetNextOrderThenReturnNextOrderGreaterThan1() {
        final Long companyKey = 1L;
        final TypeFromEnum typeFrom = TypeFromEnum.PERSON;
        when(repository.findMaxOrderByCompanyKeyAndTypeFrom(companyKey, typeFrom)).thenReturn(Optional.of(1));

        final int nextOrder = service.getNextOrder(companyKey, typeFrom);

        assertEquals(2, nextOrder, "NextOrder not equal");
    }

    @Test
    @DisplayName("Junit Test Given Key and Context When FindByKeyAndCompanyKey Then Return Question")
    void testGivenKeyAndCompanyKeyWhenFindByKeyAndCompanyKeyThenReturnQuestion() {
        mockFindByKeyAndUserCompanyKey();

        assertEquals(entity, service.findByKeyAndCompanyKey(KEY), "Entity not equal");
    }

    @Test
    @DisplayName("Junit Test Given wrong Key and Context When FindByKeyAndCompanyKey Then throw ResourceNotFoundException")
    void testGivenKeyWrongAndContextWhenFindByKeyAndCompanyKeyThenThrowResourceNotFoundException() {
        final Long wrongKey = 2L;
        mockUserServiceGetMe();

        assertThrows(ResourceNotFoundException.class, () -> service.findByKeyAndCompanyKey(wrongKey), "Does Not Throw");
    }

    @Test
    @DisplayName("Junit Test Given Key When Delete Then Does not Throw Exception")
    void testGivenKeyWhenDeleteThenDoesNotThrowException() {
        mockFindByKeyAndUserCompanyKey();

        assertDoesNotThrow(() -> service.delete(KEY), "Does Not Throw");
    }

    @Test
    @DisplayName("Junit Test Given Key When Delete Then Throw ValidException")
    void testGivenKeyWhenDeleteThenThrowValidException() {
        mockUserServiceGetMe();
        when(repository.findByKeyAndCompanyKey(KEY, KEY)).thenReturn(Optional.of(input.mockEntityWithAnswers(1)));

        final ValidException exception = assertThrows(ValidException.class, () -> service.delete(KEY),
                "Does Not Throw");
        assertNotNull(exception, "Exception is null");
        assertEquals(1, exception.getErrs().size(), "Return size of errors is different");
        assertTrue(exception.getErrs().containsAll(
                List.of("question.answers.delete_error")),
                "Return errors is different");
    }

    @Test
    @DisplayName("Junit Test Given Filter When FindPageAll Then Return Page")
    void testGivenFilterWhenFindPageAllThenReturnPage() {
        final int maxSize = 10;
        final QuestionFilterDTO filter = new QuestionFilterDTO();
        final List<QuestionListDTO> list = input.mockQuestionListDTOListWithKey(maxSize);
        final Page<QuestionListDTO> page = new PageImpl<>(list);

        mockUserServiceGetMe();
        when(repository.page(eq(filter), any(), eq(QuestionListDTO.class), eq(Question.class), anyString(), any()))
                .thenReturn(page);

        final Page<QuestionListDTO> pages = service.findPageAll(filter);
        assertNotNull(pages, "Return null");
        for (int index = 1; index <= maxSize; index++) {
            final var dto = pages.getContent().get(index - 1);
            final var entity = list.get(index - 1);
            assertEquals(dto.getLinks().toList().get(0).getHref(), "/api/v1/question/" + index,
                    "Href not equal");
            assertEquals(dto.getKey(), entity.getKey(), "Key not equal");
            assertEquals(dto.getValue(), entity.getValue(), "Value not equal");
            assertEquals(dto.getMask(), entity.getMask(), "Mask not equal");
            assertEquals(dto.getOrder(), entity.getOrder(), "Order not equal");
            assertEquals(dto.getTypeFrom(), entity.getTypeFrom(), "TypeFrom not equal");
            assertEquals(dto.getType(), entity.getType(), "Type not equal");
            assertEquals(dto.isEnabled(), entity.isEnabled(), "Enabled not equal");
            assertEquals(dto.isRequired(), entity.isRequired(), "Required not equal");
        }
    }

    private void mockUserServiceGetMe() {
        User user = new MockUser().mockEntity(1);
        user.getCompany().setKey(KEY);
        when(userService.getMe()).thenReturn(user);
    }

    private void mockFindByKeyAndUserCompanyKey() {
        mockUserServiceGetMe();
        when(repository.findByKeyAndCompanyKey(KEY, KEY)).thenReturn(Optional.of(entity));
    }
}
