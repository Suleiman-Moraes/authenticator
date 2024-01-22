package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.model.dto.KeyDTO;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonFilterDTO;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;
import com.moraes.authenticator.api.util.JsonObjectUtil;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SuppressWarnings("unchecked")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(2)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerTest extends AbstractIntegrationTest {

    private static final String NAME_KEY = "key";
    private static final String PATH_KEY = "{key}";
    private static final String BASE_URL = "/api/v1/person";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static MockPerson input;
    private static PersonDTO dto;
    private static String username = "username";
    private static String password = "654321";
    private static Long key;

    @BeforeAll
    public static void setup() {
        // Create an ObjectMapper instance
        mapper = new ObjectMapper();
        input = new MockPerson();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

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
    @DisplayName("JUnit Integration test Given PersonDTO When insert Then return key")
    void testIntegrationGivenPersonDTOWhenInsertThenReturnKey() throws Exception {
        AuthTest.checkAuth(specification, mapper);
        dto = input.mockPersonDTO(1);
        dto.getUser().setProfile(KeyDTO.builder().key(3L).build());
        dto.getUser().setUsername(username);
        dto.getUser().setPassword(password);
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
    @DisplayName("JUnit Integration test Given key When findByKey Then return PersonDTO")
    void testIntegrationGivenKeyWhenFindByKeyThenReturnPersonDTO() throws Exception {
        final Response response = findByKey();
        response.then().statusCode(200);
        final PersonDTO dtoResponse = mapper.readValue(response.getBody().asString(), PersonDTO.class);

        assertNotNull(dtoResponse, "Person is null");
        assertNotNull(dtoResponse.getUser(), "User is null");
        assertNotNull(dto.getUser().getProfile(), "Profile is null");
        assertNull(dtoResponse.getUser().getPassword(), "Password is not null");
        assertEquals(username, dtoResponse.getUser().getUsername(), "Username is null");
        assertEquals(dto.getName(), dtoResponse.getName(), "Name is different");
        assertEquals(dto.getAddress(), dtoResponse.getAddress(), "Address is different");
        assertEquals(dto.getUser().getProfile().getKey(), dtoResponse.getUser().getProfile().getKey(),
                "Profile is different");
    }

    @Test
    @Order(3)
    @DisplayName("JUnit Integration test Given PersonDTO and key When update Then return key")
    void testIntegrationGivenPersonDTOAndKeyWhenUpdateThenReturnKey() throws Exception {
        final String name = "John Doe";
        dto.setName(name);
        username = "username2";
        dto.getUser().setUsername(username);
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
    @DisplayName("JUnit Integration test Given Key When findByKey After update Then return PersonDTO with updated data")
    void testIntegrationGivenKeyWhenFindByKeyAfterUpdateThenReturnPersonDTO() throws Exception {
        testIntegrationGivenKeyWhenFindByKeyThenReturnPersonDTO();
    }

    @Test
    @Order(5)
    @DisplayName("JUnit Integration test Given PersonFilterDTO with paginate false When findAll Then return Page")
    void testIntegrationGivenPersonFilterDTOWithPaginateFalseWhenFindAllThenReturnPage() throws Exception {
        final PersonFilterDTO personFilter = new PersonFilterDTO();

        final Response response = given().spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .params(JsonObjectUtil.convertObjectToMap(personFilter))
                .when()
                .get();

        response.then().statusCode(200);

        final Map<String, Object> page = mapper.readValue(response.getBody().asString(),
                new TypeReference<>() {
                });
        final List<PersonListDTO> content = JsonObjectUtil
                .convertMapsToListOfSomething((List<Map<String, Object>>) page.get("content"), PersonListDTO.class);

        assertNotNull(page, "Page is null");
        assertNotNull(content, "Content is null");
        assertEquals(1, page.get("totalPages"), "Total pages is different");
        assertEquals(4, page.get("totalElements"), "Total elements is different");
        assertEquals(4, page.get("size"), "Size is different");
        assertEquals(0, page.get("number"), "Number is different");
        assertEquals(username, content.get(0).getUsername(), "Username is null");
        assertEquals(dto.getName(), content.get(0).getName(), "Name is different");
        assertEquals(dto.getAddress(), content.get(0).getAddress(), "Address is different");
    }

    @Test
    @Order(6)
    @DisplayName("JUnit Integration test Given PersonFilterDTO with paginate true When findAll Then return Page")
    void testIntegrationGivenPersonFilterDTOWithPaginateTrueWhenFindAllThenReturnPage() throws Exception {
        final PersonFilterDTO personFilter = PersonFilterDTO.builder()
                .paginate(true)
                .page(1)
                .size(2)
                .direction(Direction.ASC)
                .property("id")
                .build();

        final Response response = given().spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .params(JsonObjectUtil.convertObjectToMap(personFilter))
                .when()
                .get();

        response.then().statusCode(200);

        final Map<String, Object> page = mapper.readValue(response.getBody().asString(),
                new TypeReference<>() {
                });
        final List<PersonListDTO> content = JsonObjectUtil
                .convertMapsToListOfSomething((List<Map<String, Object>>) page.get("content"), PersonListDTO.class);

        assertNotNull(page, "Page is null");
        assertNotNull(content, "Content is null");
        assertEquals(2, page.get("totalPages"), "Total pages is different");
        assertEquals(4, page.get("totalElements"), "Total elements is different");
        assertEquals(2, page.get("size"), "Size is different");
        assertEquals(1, page.get("number"), "Number is different");
        assertEquals(username, content.get(1).getUsername(), "Username is null");
        assertEquals(dto.getName(), content.get(1).getName(), "Name is different");
        assertEquals(dto.getAddress(), content.get(1).getAddress(), "Address is different");
    }

    @Test
    @Order(7)
    @DisplayName("JUnit Integration test Given key When delete Then return no content")
    void testIntegrationGivenKeyWhenDeleteThenReturnNoContent() throws Exception {
        delete(key).then().statusCode(204);
    }

    @Disabled("Test disabled because it makes no sense to delete this person in an integration test")
    @Test
    @Order(7)
    @DisplayName("JUnit Integration test Given key for any person When delete Then return no content")
    void testIntegrationGivenKeyForAnyPersonWhenDeleteThenReturnNoContent() throws Exception {
        delete(3l).then().statusCode(204);
    }

    @Test
    @Order(7)
    @DisplayName("JUnit Integration test Given key for root person When delete Then return bad request")
    void testIntegrationGivenKeyForRootPersonWhenDeleteThenReturnBadRequest() throws Exception {
        delete(1l).then().statusCode(400);
    }

    @Test
    @Order(8)
    @DisplayName("JUnit Integration test Given Key When findByKey After delete Then return not found")
    void testIntegrationGivenKeyWhenFindByKeyAfterDeleteThenReturnNotFound() throws Exception {
        findByKey().then().statusCode(404);
    }

    @Test
    @Order(9)
    @DisplayName("JUnit Integration test Given PersonDTO with min values possible When insert Then return key")
    void testIntegrationGivenPersonDTOWithMinValuesPossibleWhenInsertThenReturnKey() throws Exception {
        AuthTest.checkAuth(specification, mapper);
        PersonDTO dto = input.mockPersonDTO(2);
        dto.setAddress(null);
        dto.getUser().setProfile(KeyDTO.builder().key(3L).build());
        final Response response = given().spec(specification)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .body(dto)
                .when()
                .post();

        response.then().statusCode(201);
        Long key = mapper.readValue(response.getBody().asString(), Long.class);

        assertNotNull(key, "Key is null");
        assertTrue(key > 0, "Key is not greater than zero");
        delete(key).then().statusCode(204);
    }

    private Response delete(Long key) {
        return given().spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .pathParam(NAME_KEY, key)
                .when()
                .delete(PATH_KEY);
    }

    private static Response findByKey() throws JsonProcessingException {
        return given().spec(specification)
                .pathParam(NAME_KEY, key)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get(PATH_KEY);
    }
}
