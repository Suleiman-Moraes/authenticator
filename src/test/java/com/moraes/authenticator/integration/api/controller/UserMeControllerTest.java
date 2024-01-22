package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.api.util.ConstantsUtil.BEARER;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.BASIC_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.person.PersonMeDTO;
import com.moraes.authenticator.api.model.dto.user.UserNewPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordTokenDTO;
import com.moraes.authenticator.api.repository.IUserRepository;
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
    private static PersonMeDTO personMeDTO;
    private static String bearerToken = "";

    @Autowired
    private IUserRepository repository;

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
    @DisplayName("JUnit Integration test Given UserNewPasswordDTO When changePasswordMe Then return no content")
    void testIntegrationGivenUserNewPasswordDTOWhenChangePasswordMeThenReturnNoContent() throws Exception {
        final String newPassword = String.valueOf(new Date().getTime());
        // Created PersonMeDTO
        personMeDTO = mockPerson.mockPersonMeDTO(1);
        personMeDTO.getUser().setUsername(String.valueOf(new Date().getTime()));

        // Insert that new PersonMeDTO
        PersonMeControllerTest.insertMe(specification, personMeDTO);

        // Login with that new PersonMeDTO
        final Response authResponse = AuthTest.signin(personMeDTO.getUser().getUsername(),
                personMeDTO.getUser().getPassword(), specification);
        authResponse.then().statusCode(200);

        // Get access token
        final TokenDTO token = mapper.readValue(authResponse.getBody().asString(), TokenDTO.class);
        bearerToken = String.format("%s %s", BEARER, token.getAccessToken());

        given().spec(specification)
                .header(AUTHORIZATION, bearerToken)
                .contentType(APPLICATION_JSON)
                .body(new UserNewPasswordDTO(personMeDTO.getUser().getPassword(), newPassword))
                .when()
                .patch("password")
                .then().statusCode(204);

        assertNotEquals(newPassword, personMeDTO.getUser().getPassword(), "Password is the same");

        // Try to login with that new PersonMeDTO after change password
        AuthTest.signin(personMeDTO.getUser().getUsername(), newPassword, specification).then()
                .statusCode(200);
        personMeDTO.getUser().setPassword(newPassword);
    }

    @Test
    @Order(2)
    @DisplayName("JUnit Integration test Given UserResetPasswordDTO When resetPassword Then return no content")
    void testIntegrationGivenUserResetPasswordDTOWhenResetPasswordThenReturnNoContent() throws Exception {
        given().spec(specification)
                .header(AUTHORIZATION, BASIC_TOKEN).contentType(APPLICATION_JSON)
                .body(new UserResetPasswordDTO(personMeDTO.getUser().getUsername(), personMeDTO.getEmail()))
                .when()
                .patch("password/reset")
                .then().statusCode(204);
    }

    @Test
    @Order(3)
    @DisplayName("JUnit Integration test Given UserResetPasswordTokenDTO When resetPasswordToken Then return no content")
    void testIntegrationGivenUserResetPasswordTokenDTOWhenResetPasswordTokenThenReturnNoContent() throws Exception {
        final String password = "1234567";
        final UUID token = repository.findByUsername(personMeDTO.getUser().getUsername()).orElse(new User())
                .getTokenResetPassword();
        given().spec(specification)
                .header(AUTHORIZATION, BASIC_TOKEN).contentType(APPLICATION_JSON)
                .body(new UserResetPasswordTokenDTO(password,
                        token))
                .when()
                .patch("password/reset/token")
                .then().statusCode(204);
        personMeDTO.getUser().setPassword(password);
    }

    @Test
    @Order(4)
    @DisplayName("JUnit Integration test Given Context When updateDisabledMe for disabled Then return no content")
    void testIntegrationGivenContextWhenUpdateDisabledMeForDisabledThenReturnNoContent() throws Exception {
        // Update disabled that new PersonMeDTO
        given().spec(specification)
                .header(AUTHORIZATION, bearerToken)
                .when()
                .patch("disabled")
                .then().statusCode(204);

        // Try to login with that new PersonMeDTO after disabled
        AuthTest.signin(personMeDTO.getUser().getUsername(), personMeDTO.getUser().getPassword(), specification).then()
                .statusCode(403);
    }
}
