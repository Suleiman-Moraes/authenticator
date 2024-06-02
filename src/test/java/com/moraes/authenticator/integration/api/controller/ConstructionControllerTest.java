package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.moraes.authenticator.api.controller.ConstructionController;
import com.moraes.authenticator.api.mock.real_state.MockConstruction;
import com.moraes.authenticator.api.mock.real_state.MockProposal;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.config.AbstractIntegrationTest;
import com.moraes.authenticator.config.TestConfig;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * This class definition defines a test integration class for the
 * {@link ConstructionController}
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(11)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ConstructionControllerTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/construction";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;

    @BeforeAll
    public static void setup() {
        // Create an ObjectMapper instance
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
    @DisplayName("Creating a Proposal with a specific construction company")
    void testCreateProposalWithSpecificConstruction() throws Exception {
        AuthTest.checkAuth(specification, mapper);

        ProposalDTO proposalDTO = new MockProposal().mockProposalDTO(1);
        proposalDTO.getEnterprise().setConstructionName(MockConstruction.INTEGRATION_NAME);

        ProposalControllerTest.insert(specification, proposalDTO);
    }

    @Test
    @Order(2)
    @DisplayName("JUnit Integration test Given context When getNameAll Then return list of construction names")
    void testIntegrationGivenContextWhenGetNameAllThenReturnListOfConstructionNames() throws Exception {

        final Response response = given().spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get("/name");

        response.then().statusCode(200);

        final List<String> names = mapper.readValue(response.getBody().asString(), new TypeReference<>() {
        });

        assertNotNull(names, "Names is null");
        assertFalse(names.isEmpty(), "Names is empty");
        assertTrue(names.contains(MockConstruction.INTEGRATION_NAME),
                "Names does not contain ".concat(MockConstruction.INTEGRATION_NAME));
    }
}
