package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
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
import com.moraes.authenticator.api.model.dto.user.UserEnabledDTO;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(6)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserControllerTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/user";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static MockPerson mockPerson;
    private static Long key;
    private static PersonMeDTO personMeDTO;

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
    @DisplayName("JUnit Integration test Given Key and boolean false When updateEnabled Then return no content")
    void testIntegrationGivenKeyAndBooleanFalseWhenUpdateEnabledThenReturnNoContent() throws Exception {
        // Created PersonMeDTO
        personMeDTO = mockPerson.mockPersonMeDTO(1);
        personMeDTO.getUser().setUsername(String.valueOf(new Date().getTime()));

        // Insert that new PersonMeDTO
        key = mapper.readValue(PersonMeControllerTest.insertMe(specification, personMeDTO).getBody().asString(),
                Long.class);

        // Login
        AuthTest.checkAuth(specification, mapper);

        // Update disabled that new PersonMeDTO
        updateEnabled(new UserEnabledDTO(false));

        // Try to login with that new PersonMeDTO after disabled
        AuthTest.signin(personMeDTO.getUser().getUsername(), personMeDTO.getUser().getPassword(), specification).then()
                .statusCode(403);
    }

    @Test
    @Order(2)
    @DisplayName("JUnit Integration test Given Key and boolean true When updateEnabled Then return no content")
    void testIntegrationGivenKeyAndBooleanTrueWhenUpdateEnabledThenReturnNoContent() throws Exception {
        // Update enabled that new PersonMeDTO
        updateEnabled(new UserEnabledDTO(true));

        // Try to login with that new PersonMeDTO after disabled
        AuthTest.signin(personMeDTO.getUser().getUsername(), personMeDTO.getUser().getPassword(), specification).then()
                .statusCode(200);
    }

    private void updateEnabled(UserEnabledDTO userEnabledDTO) {
        given().spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(APPLICATION_JSON)
                .pathParam("key", key)
                .body(userEnabledDTO)
                .when()
                .patch("enabled/{key}")
                .then().statusCode(204);
    }
}
