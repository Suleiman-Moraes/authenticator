package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
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
import com.moraes.authenticator.api.mock.MockProfile;
import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileFilterDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.util.JsonObjectUtil;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

@SuppressWarnings({ "rawtypes", "unchecked" })
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

    @Test
    @Order(5)
    @DisplayName("JUnit Integration test Given ProfileFilterDTO with paginate false When findPageAll Then return Page<KeyDescriptionDTO>")
    void testIntegrationGivenProfileFilterDTOWithPaginateFalseWhenFindPageAllThenReturnPageKeyDescriptionDTO()
            throws Exception {
        final ProfileFilterDTO filter = new ProfileFilterDTO();
        final Response response = findPageAll(filter);

        final Map<String, Object> page = mapper.readValue(response.getBody().asString(),
                new TypeReference<>() {
                });
        final List<KeyDescriptionDTO> content = JsonObjectUtil
                .convertMapsToListOfSomething((List<Map<String, Object>>) page.get("content"), KeyDescriptionDTO.class);

        assertNotNull(page, "Page is null");
        assertNotNull(content, "Content is null");
        assertEquals(1, page.get("totalPages"), "Total pages is different");
        assertEquals(4, page.get("totalElements"), "Total elements is different");
        assertEquals(4, page.get("size"), "Size is different");
        assertEquals(0, page.get("number"), "Number is different");
        assertEquals(dto.getDescription(), content.get(0).getDescription(), "Description is different");
        assertEquals(key.toString(), content.get(0).getKey().toString(), "Key is different");
    }

    @Test
    @Order(6)
    @DisplayName("JUnit Integration test Given ProfileFilterDTO with paginate true When findPageAll Then return Page<KeyDescriptionDTO>")
    void testIntegrationGivenProfileFilterDTOWithPaginateTrueWhenFindPageAllThenReturnPageKeyDescriptionDTO()
            throws Exception {
        final ProfileFilterDTO filter = ProfileFilterDTO.builder()
                .paginate(true)
                .page(1)
                .size(2)
                .direction(Direction.ASC)
                .property("id")
                .build();
        final Response response = findPageAll(filter);

        final Map<String, Object> page = mapper.readValue(response.getBody().asString(),
                new TypeReference<>() {
                });
        final List<KeyDescriptionDTO> content = JsonObjectUtil
                .convertMapsToListOfSomething((List<Map<String, Object>>) page.get("content"), KeyDescriptionDTO.class);

        assertNotNull(page, "Page is null");
        assertNotNull(content, "Content is null");
        assertEquals(2, page.get("totalPages"), "Total pages is different");
        assertEquals(4, page.get("totalElements"), "Total elements is different");
        assertEquals(2, page.get("size"), "Size is different");
        assertEquals(1, page.get("number"), "Number is different");
        assertEquals(dto.getDescription(), content.get(1).getDescription(), "Description is different");
        assertEquals(key.toString(), content.get(1).getKey().toString(), "Key is different");
    }

    @Test
    @Order(7)
    @DisplayName("JUnit Integration test Given Key When delete Then return no content")
    void testIntegrationGivenKeyWhenDeleteThenReturnNoContent() throws Exception {
        delete(key).then().statusCode(204);
    }

    @Test
    @Order(8)
    @DisplayName("JUnit Integration test Given Key for used profile When delete Then return bad request")
    void testIntegrationGivenKeyForUsedProfileWhenDeleteThenReturnBadRequest() throws Exception {
        delete(1l).then().statusCode(400);
    }

    private Response delete(Long key) {
        return given().spec(specification)
                .pathParam(NAME_KEY, key)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .delete(PATH_KEY);
    }

    private Response findPageAll(ProfileFilterDTO filter) {
        return given().spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .params(JsonObjectUtil.convertObjectToMap(filter))
                .when()
                .get();
    }

    private static Response findByKey() throws JsonProcessingException {
        final Response response = given().spec(specification)
                .pathParam(NAME_KEY, key)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get(PATH_KEY);
        response.then().statusCode(200);
        return response;
    }
}
