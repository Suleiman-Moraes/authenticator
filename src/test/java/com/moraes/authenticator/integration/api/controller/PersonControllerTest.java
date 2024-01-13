package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.model.dto.KeyDTO;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(2)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonControllerTest extends AbstractIntegrationTest {

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
        final PersonDTO dtoResponse = findByKey();

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
                .pathParam("key", key)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .body(dto)
                .when()
                .put("{key}");
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

    private static PersonDTO findByKey() throws JsonProcessingException {
        final Response response = given().spec(specification)
                .pathParam("key", key)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get("{key}");

        response.then().statusCode(200);
        return mapper.readValue(response.getBody().asString(), PersonDTO.class);
    }
}
