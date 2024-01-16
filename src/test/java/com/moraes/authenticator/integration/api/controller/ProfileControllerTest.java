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
import com.moraes.authenticator.api.mock.MockProfile;
import com.moraes.authenticator.api.model.dto.profile.ProfileDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(3)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProfileControllerTest extends AbstractIntegrationTest {

    private static final String NAME_KEY = "key";
    private static final String PATH_KEY = "{key}";
    private static final String BASE_URL = "/api/v1/profile";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static MockProfile input;
    private static ProfileDTO dto;
    private static Long key;

    @BeforeAll
    public static void setup() {
        // Create an ObjectMapper instance
        mapper = new ObjectMapper();
        input = new MockProfile();
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
    @DisplayName("JUnit Integration test Given ProfileDTO When insert Then return key")
    void testIntegrationGivenProfileDTOWhenInsertThenReturnKey() throws Exception {
        AuthTest.checkAuth(specification, mapper);
        dto = input.mockProfileDTO(1);
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
    @DisplayName("JUnit Integration test Given key When findByKey Then return ProfileDTO")
    void testIntegrationGivenKeyWhenFindByKeyThenReturnProfileDTO() throws Exception {
        final Response response = findByKey();
        response.then().statusCode(200);
        final ProfileDTO dtoResponse = mapper.readValue(response.getBody().asString(), ProfileDTO.class);

        assertNotNull(dtoResponse, "Profile is null");
        assertNotNull(dtoResponse.getRoles(), "Roles is null");
        assertEquals(dtoResponse.getDescription(), dto.getDescription(), "Description is different");
        assertEquals(dtoResponse.getRoles(), dto.getRoles(), "Roles is different");
    }

    @Test
    @Order(3)
    @DisplayName("JUnit Integration test Given ProfileDTO and key When update Then return key")
    void testIntegrationGivenProfileDTOAndKeyWhenUpdateThenReturnKey() throws Exception {
        dto.setDescription("Profile Description 2");
        dto.getRoles().add(RoleEnum.COMMON_USER);

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
    @DisplayName("JUnit Integration test Given Key When findByKey After update Then return ProfileDTO with updated data")
    void testIntegrationGivenKeyWhenFindByKeyAfterUpdateThenReturnProfileDTO() throws Exception {
        testIntegrationGivenKeyWhenFindByKeyThenReturnProfileDTO();
    }

    private static Response findByKey() throws JsonProcessingException {
        return given().spec(specification)
                .pathParam(NAME_KEY, key)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get(PATH_KEY);
    }
}
