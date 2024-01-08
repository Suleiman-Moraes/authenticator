package com.moraes.authenticator.integration.api;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.util.ConstantsUtil;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;
import com.moraes.authenticator.config.security.dto.AccountCredentialsDTO;
import com.moraes.authenticator.config.security.dto.TokenDTO;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@Order(1)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthTest extends AbstractIntegrationTest {

    public static final String BASIC_TOKEN = "Basic d2ViOjEyMzQ1Ng==";

    private static String ACCESS_TOKEN = "";
    private static String REFRESH_TOKEN = "";
    private static final String BASE_PATH = "/auth";
    private static final String USERNAME = "admin";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;

    /**
     * Initializes the necessary objects and configurations for the test.
     */
    @BeforeAll
    public static void setup() {
        // Create an ObjectMapper instance
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // Create a RequestSpecification instance
        specification = new RequestSpecBuilder()
                .setBasePath(BASE_PATH)
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @DisplayName("JUnit Integration test Given AccountCredentialsDTO When signin Then return TokenDTO")
    @Test
    @Order(1)
    void testIntegrationGivenAccountCredentialsDTOWhenSigninThenReturnTokenDTO() throws Exception {
        final Response response = signin(USERNAME, specification);

        response.then().statusCode(200);
        final TokenDTO token = mapper.readValue(response.getBody().asString(), TokenDTO.class);

        assertNotNull("Token is null", token);
        assertNotNull("Access Token is null", token.getAccessToken());
        assertNotNull("Refresh Token is null", token.getRefreshToken());
        assertTrue("Unauthenticated", token.isAuthenticated());
        assertTrue("Token expired", new Date().before(token.getExpiration()));
        assertEquals("Username not equal", USERNAME, token.getUsername());
        ACCESS_TOKEN = token.getAccessToken();
        REFRESH_TOKEN = token.getRefreshToken();
    }

    @DisplayName("JUnit Integration test Given refreshToken When refreshToken Then return TokenDTO")
    @Test
    @Order(2)
    void testIntegrationGivenRefreshTokenWhenRefreshTokenThenReturnTokenDTO() throws Exception {
        final Response response = given()
                .spec(specification)
                .basePath(concatPath("/refresh"))
                .header(ConstantsUtil.AUTHORIZATION, String.format("Bearer %s", REFRESH_TOKEN))
                .when()
                .put();

        response.then().statusCode(200);
        final TokenDTO token = mapper.readValue(response.getBody().asString(), TokenDTO.class);

        assertNotNull("Token is null", token);
        assertNotNull("Access Token is null", token.getAccessToken());
        assertNotNull("Refresh Token is null", token.getRefreshToken());
        assertTrue("Unauthenticated", token.isAuthenticated());
        assertTrue("Token expired", new Date().before(token.getExpiration()));
        assertEquals("Username not equal", USERNAME, token.getUsername());
        ACCESS_TOKEN = token.getAccessToken();
        REFRESH_TOKEN = token.getRefreshToken();
    }

    @DisplayName("JUnit Integration test Given Invalid username When signin Then Throw Exception")
    @Test
    void testIntegrationGivenInvalidUsernameAccountCredentialsDTOWhenSigninThenThrowException() throws Exception {
        final Response response = signin("invalid", specification);
        response.then().statusCode(403);
    }

    @DisplayName("JUnit Integration test Given Invalid password When signin Then Throw Exception")
    @Test
    void testIntegrationGivenInvalidPasswordAccountCredentialsDTOWhenSigninThenThrowException() throws Exception {
        final Response response = signin(USERNAME, "1234567", specification);
        response.then().statusCode(403);
    }

    private static Response signin(final String username, RequestSpecification specification) {
        return signin(username, "123456", specification);
    }

    private static Response signin(final String username, final String password, RequestSpecification specification) {
        return given()
                .spec(specification)
                .basePath(concatPath("/signin"))
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(AccountCredentialsDTO.builder()
                        .username(username)
                        .password(password)
                        .build())
                .header(ConstantsUtil.AUTHORIZATION, BASIC_TOKEN)
                .when()
                .post();
    }

    private static String concatPath(String path) {
        return BASE_PATH + path;
    }

    public static String getAccessToken() {
        return String.format("Bearer %s", ACCESS_TOKEN);
    }
}
