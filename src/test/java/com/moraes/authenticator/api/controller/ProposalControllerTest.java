package com.moraes.authenticator.api.controller;

import static com.moraes.authenticator.api.util.ConstantsTestUtil.USER_MESSAGES;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.real_state.MockProposal;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;

@WebMvcTest
class ProposalControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/proposal";

    private MockProposal input;

    private MockSecurity mockSecurity;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        input = new MockProposal();
        mockSecurity = new MockSecurity();
    }

    @Test
    @DisplayName("JUnit test Given ProposalDTO When insert Then return Long")
    void testGivenProposalDTOWhenInsertThenReturnLong() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final Long key = 1L;
        final ProposalDTO object = input.mockProposalDTO(1);
        given(proposalService.insert(object)).willReturn(key);

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(object))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("%s/%s", BASE_URL, key)))
                .andExpect(jsonPath("$", is(key.intValue())));
    }

    @Test
    @DisplayName("JUnit test Given Invalid ProposalDTO When insert Then return BadRequest")
    void testGivenInvalidProposalDTOWhenInsertThenReturnBadRequest() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final ProposalDTO object = ProposalDTO.builder()
                .enterprise(new EnterpriseDTO())
                .conditions(List.of(new ConditionDTO()))
                .build();
        given(proposalService.insert(object)).willReturn(null);

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(object))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES.concat(".length()")).value(14));
    }

    @Test
    @DisplayName("JUnit test Given Invalid ProposalDTO with wrong values When insert Then return BadRequest")
    void testGivenInvalidProposalDTOWithWrongValuesWhenInsertThenReturnBadRequest() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final ProposalDTO object = input.mockProposalDTOWrongValues();
        given(proposalService.insert(object)).willReturn(null); // wrong value

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(object))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES.concat(".length()")).value(15));
    }
}
