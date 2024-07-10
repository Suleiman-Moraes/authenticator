package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.controller.QuestionController;
import com.moraes.authenticator.api.mock.menu.MockQuestion;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionFilterDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionListDTO;
import com.moraes.authenticator.api.model.enums.TypeEnum;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.util.JsonObjectUtil;
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

    private static List<Long> keys;

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
        assertEquals(dto.isEnabled(), dtoResponse.isEnabled(), "Enabled not equal");
        assertEquals(dto.isRequired(), dtoResponse.isRequired(), "Required not equal");
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

    @Test
    @Order(4)
    @DisplayName("JUnit Integration test Given Key When findByKey After update Then return QuestionDTO with updated data")
    void testIntegrationGivenKeyWhenFindByKeyAfterUpdateThenReturnQuestionDTO() throws Exception {
        testIntegrationGivenKeyWhenFindByKeyThenReturnQuestionDTO();
    }

    @Test
    @Order(5)
    @DisplayName("JUnit Integration test Given List of QuestionAllDTO and typeFrom When insertAll Then return List of keys")
    void testIntegrationGivenListOfQuestionAllDTOAndTypeFromWhenInsertAllThenReturnListOfKeys() throws Exception {
        final Response response = given().spec(specification)
                .queryParams("typeFrom", TypeFromEnum.PERSON)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .body(input.mockQuestionAllDTOList(2))
                .when()
                .post("all");

        response.then().statusCode(201);
        keys = mapper.readValue(response.getBody().asString(),
                new TypeReference<>() {
                });

        assertNotNull(keys, "Keys is null");
        assertEquals(2, keys.size(), "Size is different");
        assertTrue(keys.get(0) > key, "Key[0] is not greater than original key");
        assertTrue(keys.get(1) > keys.get(0), "Key[1] is not greater than key[0]");
    }

    @SuppressWarnings("unchecked")
    @Test
    @Order(6)
    @DisplayName("JUnit Integration test Given QuestionFilterDTO with paginate false When findAll Then return Page")
    void testIntegrationGivenQuestionFilterDTOWithPaginateFalseWhenFindAllThenReturnPage() throws Exception {
        final QuestionFilterDTO personFilter = new QuestionFilterDTO();

        final Response response = given().spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .params(JsonObjectUtil.convertObjectToMap(personFilter))
                .when()
                .get();

        response.then().statusCode(200);

        final Map<String, Object> page = mapper.readValue(response.getBody().asString(),
                new TypeReference<>() {
                });
        final List<QuestionListDTO> content = JsonObjectUtil
                .convertMapsToListOfSomething((List<Map<String, Object>>) page.get("content"), QuestionListDTO.class);

        assertNotNull(page, "Page is null");
        assertNotNull(content, "Content is null");
        assertEquals(1, page.get("totalPages"), "Total pages is different");
        assertEquals(4, page.get("totalElements"), "Total elements is different");
        assertEquals(4, page.get("size"), "Size is different");
        assertEquals(0, page.get("number"), "Number is different");

        assertEquals(dto.getValue(), content.get(2).getValue(), "Value is different");
        assertEquals(dto.getMask(), content.get(2).getMask(), "Mask is different");
        assertEquals(dto.getOrder(), content.get(2).getOrder(), "Order is different");
        assertEquals(dto.getTypeFrom(), content.get(2).getTypeFrom(), "TypeFrom is different");
        assertEquals(dto.getType(), content.get(2).getType(), "Type is different");
    }

    @Test
    @Order(7)
    @DisplayName("JUnit Integration test Given key When delete Then return no content")
    void testIntegrationGivenKeyWhenDeleteThenReturnNoContent() throws Exception {
        delete(key).then().statusCode(204);
    }

    @Test
    @Order(8)
    @DisplayName("JUnit Integration test Given Key When findByKey After delete Then return not found")
    void testIntegrationGivenKeyWhenFindByKeyAfterDeleteThenReturnNotFound() throws Exception {
        findByKey().then().statusCode(404);
    }

    @Test
    @Order(100)
    @DisplayName("JUnit Integration test Given Nothing When After all Then clear database")
    void testIntegrationGivenNothingWhenAfterAllThenClearDatabase() throws Exception {
        keys.forEach(this::clearByKey);
    }

    private void clearByKey(Long key) {
        try {
            delete(key).then().statusCode(204);
            findByKey(key).then().statusCode(404);
        } catch (Exception e) {
            assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    private Response delete(Long key) {
        return delete(key, ACCESS_TOKEN);
    }

    private static Response findByKey() {
        return findByKey(key);
    }

    private static Response findByKey(Long key) {
        return given().spec(specification)
                .pathParam(NAME_KEY, key)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get(PATH_KEY);
    }

    private Response delete(Long key, String token) {
        return given().spec(specification)
                .header(AUTHORIZATION, token)
                .pathParam(NAME_KEY, key)
                .when()
                .delete(PATH_KEY);
    }
}
