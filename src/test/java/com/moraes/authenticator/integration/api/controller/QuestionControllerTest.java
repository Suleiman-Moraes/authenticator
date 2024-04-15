package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.controller.QuestionController;
import com.moraes.authenticator.api.mock.menu.MockQuestion;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.enums.TypeEnum;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * This class definition defines a test integration class for the
 * {@link QuestionController}
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(8)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class QuestionControllerTest extends AbstractIntegrationTest {

    private static final String NAME_KEY = "key";
    private static final String PATH_KEY = "{key}";
    private static final String BASE_URL = "/api/v1/question";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static MockQuestion input;
    private static QuestionDTO dto;
    private static Long key;

    @BeforeAll
    public static void setup() {
        // Create an ObjectMapper instance
        mapper = new ObjectMapper();
        input = new MockQuestion();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        dto = input.mockQuestionDTO(1);
        dto.setOrder(2);

        // Create a RequestSpecification instance
        specification = new RequestSpecBuilder()
                .setBasePath(BASE_URL)
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("JUnit Integration test Given QuestionDTO When insert Then return key")
    void testIntegrationGivenQuestionDTOWhenInsertThenReturnKey() throws Exception {
        AuthTest.checkAuth(specification, mapper);

        final Response response = given().spec(specification)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .body(dto)
                .when()
                .post();

        response.then().statusCode(201);
        key = mapper.readValue(response.getBody().asString(), Long.class);

        assertNotNull(key, "Key is null");
        assertTrue(key > 0, "Key is not greater than zero");
    }

    @Test
    @Order(2)
    @DisplayName("JUnit Integration test Given key When findByKey Then return QuestionDTO")
    void testIntegrationGivenKeyWhenFindByKeyThenReturnQuestionDTO() throws Exception {
        final Response response = findByKey();
        response.then().statusCode(200);

        final QuestionDTO dtoResponse = mapper.readValue(response.getBody().asString(), QuestionDTO.class);

        assertNotNull(dtoResponse, "DTO is null");
        assertEquals(dto.getValue(), dtoResponse.getValue(), "Value not equal");
        assertEquals(dto.getMask(), dtoResponse.getMask(), "Mask not equal");
        assertEquals(dto.getOrder(), dtoResponse.getOrder(), "Order not equal");
        assertEquals(dto.getTypeFrom(), dtoResponse.getTypeFrom(), "TypeFrom not equal");
        assertEquals(dto.getType(), dtoResponse.getType(), "Type not equal");
        assertTrue(dtoResponse.isEnabled(), "Enabled not equal");
        assertTrue(dtoResponse.isRequired(), "Required not equal");
    }

    @Test
    @Order(3)
    @DisplayName("JUnit Integration test Given QuestionDTO and key When update Then return key")
    void testIntegrationGivenQuestionDTOAndKeyWhenUpdateThenReturnKey() throws Exception {

        final String newValue = "newValue";
        final String newMask = "newMask";
        final Integer newOrder = 3;
        final TypeEnum newType = TypeEnum.TIME;
        final boolean newEnabled = false;
        final boolean newRequired = true;

        dto.setValue(newValue);
        dto.setMask(newMask);
        dto.setOrder(newOrder);
        dto.setType(newType);
        dto.setEnabled(newEnabled);
        dto.setRequired(newRequired);

        final Response response = given().spec(specification)
                .pathParam(NAME_KEY, key)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .body(dto)
                .when()
                .put(PATH_KEY);
        response.then().statusCode(200);
        final Long newKey = mapper.readValue(response.getBody().asString(), Long.class);
        assertEquals(key, newKey, "Key is not equal");
    }

    private static Response findByKey() throws JsonProcessingException {
        return given().spec(specification)
                .pathParam(NAME_KEY, key)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get(PATH_KEY);
    }
}
