package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN_ME;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.BASIC_TOKEN;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.PASSWORD_ME;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.USERNAME_ME;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.setAccessTokenMe;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonMeDTO;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;
import com.moraes.authenticator.config.security.dto.TokenDTO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Order(2)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PersonMeControllerTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/person/me";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static MockPerson input;
    PersonMeDTO dto;

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
    @DisplayName("JUnit Integration test Given PersonMeDTO When insertMe Then return key")
    void testIntegrationGivenPersonMeDTOWhenInsertMeThenReturnKey() throws Exception {
        dto = input.mockPersonMeDTO(1);
        dto.getUser().setUsername(USERNAME_ME);
        dto.getUser().setPassword(PASSWORD_ME);
        final Response response = given().spec(specification)
                .basePath(BASE_URL.concat("/new"))
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, BASIC_TOKEN)
                .body(dto)
                .when()
                .post();

        response.then().statusCode(201);
        final Long key = mapper.readValue(response.getBody().asString(), Long.class);

        assertNotNull(key, "Key is null");
        assertTrue(key > 0, "Key is not greater than zero");
    }

    @Test
    @Order(2)
    @DisplayName("JUnit Integration test Given Context When getMe Then return PersonDTO")
    void testIntegrationGivenContextWhenGetMeThenReturnPersonDTO() throws Exception {
        final TokenDTO token = AuthTest.signin(USERNAME_ME, PASSWORD_ME, specification, mapper);
        setAccessTokenMe(token.getAccessToken());
        final Response response = given().spec(specification)
                .basePath(BASE_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, ACCESS_TOKEN_ME)
                .when()
                .get();

        response.then().statusCode(200);
        final PersonDTO person = mapper.readValue(response.getBody().asString(), PersonDTO.class);

        assertNotNull(person, "Person is null");
        assertNotNull(person.getUser(), "User is null");
        assertNull(person.getUser().getPassword(), "Password is not null");
        assertEquals(USERNAME_ME, person.getUser().getUsername(), "Username is null");
        assertEquals(dto.getName(), person.getName(), "Name is different");
        assertEquals(dto.getAddress(), person.getAddress(), "Address is different");
    }
}
