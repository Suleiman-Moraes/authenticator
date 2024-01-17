package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.api.util.ConstantsUtil.BEARER;
import static io.restassured.RestAssured.given;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.mock.MockPerson;
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

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(5)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserMeControllerTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/user/me";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static MockPerson mockPerson;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
        mockPerson = new MockPerson();
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
    @DisplayName("JUnit Integration test Given Context When updateDisabledMe for disabled Then return no content")
    void testIntegrationGivenContextWhenUpdateDisabledMeForDisabledThenReturnNoContent() throws Exception {
        // Created PersonMeDTO
        PersonMeDTO personMeDTO = mockPerson.mockPersonMeDTO(1);
        personMeDTO.getUser().setUsername(String.valueOf(new Date().getTime()));

        // Insert that new PersonMeDTO
        PersonMeControllerTest.insertMe(specification, personMeDTO);

        // Login with that new PersonMeDTO
        final Response authResponse = AuthTest.signin(personMeDTO.getUser().getUsername(),
                personMeDTO.getUser().getPassword(), specification);
        authResponse.then().statusCode(200);

        // Get access token
        final TokenDTO token = mapper.readValue(authResponse.getBody().asString(), TokenDTO.class);

        // Update disabled that new PersonMeDTO
        given().spec(specification)
                .header(AUTHORIZATION, String.format("%s %s", BEARER, token.getAccessToken()))
                .when()
                .patch("disabled")
                .then().statusCode(204);

        // Try to login with that new PersonMeDTO after disabled
        AuthTest.signin(personMeDTO.getUser().getUsername(), personMeDTO.getUser().getPassword(), specification).then()
                .statusCode(403);
    }
}
