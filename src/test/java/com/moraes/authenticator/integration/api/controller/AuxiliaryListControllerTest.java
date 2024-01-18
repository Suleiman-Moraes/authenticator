package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(7)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuxiliaryListControllerTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/auxiliary-list";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;

    @BeforeAll
    public static void setup() {
        mapper = new ObjectMapper();
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
    @DisplayName("JUnit Integration test When getListRoleEnum Then return list role enum")
    void testIntegrationWhenGetListRoleEnumThenReturnListRoleEnum() throws Exception {
        AuthTest.checkAuth(specification, mapper);
        final Response response = given()
                .spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get("role-enum");

        response.then()
                .statusCode(200);

        final List<KeyDescriptionDTO<String>> list = mapper.readValue(response.getBody().asString(),
                new TypeReference<>() {
                });

        assertEquals(2, list.size(), "Size is different");
        assertEquals("Admin", list.get(0).getDescription(), "Description is different");
        assertEquals("ADMIN", list.get(0).getKey(), "Key is different");
        assertEquals("Common user", list.get(1).getDescription(), "Description is different");
        assertEquals("COMMON_USER", list.get(1).getKey(), "Key is different");
    }
}
