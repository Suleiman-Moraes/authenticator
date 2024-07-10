package com.moraes.authenticator.integration.api.controller;

import static com.moraes.authenticator.api.util.ConstantsUtil.APPLICATION_JSON;
import static com.moraes.authenticator.api.util.ConstantsUtil.AUTHORIZATION;
import static com.moraes.authenticator.integration.api.IntegrationContextHolder.ACCESS_TOKEN;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionFilterDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionListDTO;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalFilterDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalListDTO;
import com.moraes.authenticator.api.util.JsonObjectUtil;
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
import com.moraes.authenticator.api.controller.ProposalController;
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

import java.util.List;
import java.util.Map;

/**
 * This class definition defines a test integration class for the
 * {@link ProposalController}
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(9)
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProposalControllerTest extends AbstractIntegrationTest {

    private static final String NAME_KEY = "key";
    private static final String PATH_KEY = "{key}";
    private static final String BASE_URL = "/api/v1/proposal";

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static MockProposal input;
    private static ProposalDTO dto;
    private static Long key;

    @BeforeAll
    public static void setup() {
        // Create an ObjectMapper instance
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        input = new MockProposal();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        dto = input.mockProposalDTO(1);

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
    @DisplayName("JUnit Integration test Given ProposalDTO When insert Then return key")
    void givenProposalDTOWhenInsertThenReturnKey() throws Exception {
        AuthTest.checkAuth(specification, mapper);

        key = mapper.readValue(insert(specification, dto).getBody().asString(), Long.class);

        assertNotNull(key, "Key is null");
        assertTrue(key > 0, "Key is not greater than zero");
    }

    @Test
    @Order(2)
    @DisplayName("JUnit Integration test Given key When findByKey Then return ProposalDTO")
    void givenKeyWhenFindByKeyThenReturnProposalDTO() throws Exception {
        final Response response = findByKey();
        response.then().statusCode(200);

        final ProposalDTO dtoResponse = mapper.readValue(response.getBody().asString(), ProposalDTO.class);

        assertNotNull(dtoResponse, "DTO is null");
        assertEquals(dto.getValue().doubleValue(), dtoResponse.getValue().doubleValue(), "Value not equal");
        assertEquals(dto.getVpl().doubleValue(), dtoResponse.getVpl().doubleValue(), "Vpl not equal");
        assertEquals(dto.getValueM2().doubleValue(), dtoResponse.getValueM2().doubleValue(), "ValueM2 not equal");
        assertEquals(dto.getSizeM2().doubleValue(), dtoResponse.getSizeM2().doubleValue(), "SizeM2 not equal");
        assertNotNull(dtoResponse.getConditions(), "Conditions is null");

        final ConditionDTO conditionResponse = dtoResponse.getConditions().get(0);
        final ConditionDTO condition = dto.getConditions().get(0);

        assertNotNull(conditionResponse, "Condition is null");
        assertEquals(dto.getConditions().size(), dtoResponse.getConditions().size(), "Conditions not equal");
        assertEquals(condition.getFrequency(), conditionResponse.getFrequency(), "Frequency not equal");
        assertEquals(condition.getNumberInstallments(), conditionResponse.getNumberInstallments(), "NumberInstallments not equal");
        assertEquals(condition.getValueInstallments().doubleValue(), conditionResponse.getValueInstallments().doubleValue(), "ValueInstallments not equal");
        assertEquals(condition.getBeginningInstallment(), conditionResponse.getBeginningInstallment(), "BeginningInstallment not equal");
    }

    @SuppressWarnings("unchecked")
    @Test
    @Order(3)
    @DisplayName("JUnit Integration test Given ProposalFilterDTO with paginate false When findAll Then return Page")
    void givenProposalFilterDTOWithPaginateFalseWhenFindAllThenReturnPage() throws Exception {
        final ProposalFilterDTO proposalFilter = new ProposalFilterDTO();

        final Response response = given().spec(specification)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .params(JsonObjectUtil.convertObjectToMap(proposalFilter))
                .when()
                .get();

        response.then().statusCode(200);

        final Map<String, Object> page = mapper.readValue(response.getBody().asString(),
                new TypeReference<>() {
                });
        final List<ProposalListDTO> content = JsonObjectUtil
                .convertMapsToListOfSomething((List<Map<String, Object>>) page.get("content"), ProposalListDTO.class);

        assertNotNull(page, "Page is null");
        assertNotNull(content, "Content is null");
        assertTrue(Integer.parseInt(page.get("totalPages").toString()) > 0, "Total pages is not greater than zero");
        assertTrue(Integer.parseInt(page.get("totalElements").toString()) > 0, "Total elements is not greater than zero");
        assertTrue(Integer.parseInt(page.get("size").toString()) > 0, "Size is not greater than zero");
        assertEquals(0, page.get("number"), "Number is different");
    }

    public static Response insert(RequestSpecification specification, ProposalDTO proposalDTO) {
        final Response response = given().spec(specification)
                .basePath(BASE_URL)
                .contentType(APPLICATION_JSON)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .body(proposalDTO)
                .when()
                .post();

        response.then().statusCode(201);
        return response;
    }

    private static Response findByKey() {
        return findByKey(key);
    }

    private static Response findByKey(Long key) {
        return given().spec(specification)
                .pathParam(NAME_KEY, key)
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .when()
                .get(PATH_KEY);
    }
}
