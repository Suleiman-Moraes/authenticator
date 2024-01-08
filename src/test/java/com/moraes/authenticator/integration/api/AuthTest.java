package com.moraes.authenticator.integration.api;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthTest extends AbstractIntegrationTest {

    public static final String BASIC_TOKEN = "Basic d2ViOjEyMzQ1Ng==";
    private static final String BASE_PATH = "/auth";

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
    void testIntegrationGivenAccountCredentialsDTOWhenSigninThenReturnTokenDTO() throws Exception {
        final String username = "admin";
        final Response response = signin(username, specification);

        response.then().statusCode(200);
        final TokenDTO token = mapper.readValue(response.getBody().asString(), TokenDTO.class);

        assertNotNull("Return null", token);
        assertNotNull("Return null", token.getAccessToken());
        assertNotNull("Return null", token.getRefreshToken());
        assertTrue("Return false", token.isAuthenticated());
        assertTrue("Return false", new Date().before(token.getExpiration()));
    }

    private static Response signin(final String username, RequestSpecification specification) {
        return given()
                .spec(specification)
                .basePath(concatPath("/signin"))
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(AccountCredentialsDTO.builder()
                        .username(username)
                        .password("123456")
                        .build())
                .header(ConstantsUtil.AUTHORIZATION, BASIC_TOKEN)
                .when()
                .post();
    }

    private static String concatPath(String path) {
        return BASE_PATH + path;
    }
}
