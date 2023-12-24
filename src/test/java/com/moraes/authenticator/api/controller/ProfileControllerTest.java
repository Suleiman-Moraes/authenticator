package com.moraes.authenticator.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.mock.MockKeyDescriptionDTO;
import com.moraes.authenticator.api.mock.MockProfile;
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileFilterDTO;

@WebMvcTest
class ProfileControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/profile";

    private static final String BASE_URL_KEY = BASE_URL + "/{key}";

    private MockProfile input;

    private MockSecurity mockSecurity;
    private MockKeyDescriptionDTO mockKeyDescriptionDTO;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        input = new MockProfile();
        mockSecurity = new MockSecurity();
        mockKeyDescriptionDTO = new MockKeyDescriptionDTO();
        // Mock Auth
        mockSecurity.mockSuperUser();
    }

    @Test
    @DisplayName("JUnit test Given profile key When get findByKey Then return ProfileDTO")
    void testGivenProfileKeyWhenGetFindByKeyThenReturnProfileDTO() throws Exception {
        // Given / Arrange
        final Profile profile = input.mockEntity(1);
        final ProfileDTO profileDTO = input.mockProfileDTO(1);

        // Mock Auth
        mockSecurity.mockSuperUser();

        given(profileService.findByKey(anyLong())).willReturn(profile);
        given(profileService.parseObject(profile, ProfileDTO.class, ProfileController.class)).willReturn(profileDTO);

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL_KEY, 1L));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(profileDTO.getDescription())));
    }

    @SuppressWarnings("rawtypes")
    @Test
    @DisplayName("JUnit test Given ProfileFilterDTO When findAll Then return Page<KeyDescriptionDTO>")
    void testGivenProfileFilterDTOWhenFindAllThenReturnPageKeyDescriptionDTO() throws Exception {
        // Given / Arrange
        final ProfileFilterDTO filter = new ProfileFilterDTO();
        final List<KeyDescriptionDTO> list = mockKeyDescriptionDTO.mockList();
        final Page<KeyDescriptionDTO> page = new PageImpl<>(list, PageRequest.of(0, 200), list.size());

        // Mock Auth
        mockSecurity.mockSuperUser();

        given(profileService.findPageAll(filter)).willReturn(page);

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].key", is(1)))
                .andExpect(jsonPath("$.content[0].description", is("1")));
    }

    @Test
    @DisplayName("JUnit test Given ProfileDTO When insert Then return Long")
    void testGivenProfileDTOWhenInsertThenReturnLong() throws Exception {

        // Given / Arrange
        final ProfileDTO profileDTO = input.mockProfileDTO(1);
        given(profileService.insert(input.mockEntity(1))).willReturn(1L);
        final String json = objectMapper.writeValueAsString(profileDTO);

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()));

        // Then / Assert
        response.andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    @DisplayName("JUnit test Given ProfileDTO When update Then return Long")
    void testGivenProfileDTOWhenUpdateThenReturnLong() throws Exception {

        // Given / Arrange
        final ProfileDTO profileDTO = input.mockProfileDTO(1);
        final String json = objectMapper.writeValueAsString(profileDTO);

        // When / Act
        ResultActions response = mockMvc.perform(put(BASE_URL_KEY, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(csrf()));

        // Then / Assert
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    @DisplayName("JUnit test Given profile key When delete Then return Void")
    void testGivenProfileKeyWhenDeleteThenReturnVoid() throws Exception {

        // Given / Arrange
        final Long key = 1L;

        // When / Act
        ResultActions response = mockMvc.perform(delete(BASE_URL_KEY, key)
                .with(csrf()));

        // Then / Assert
        response.andDo(print()).andExpect(status().isNoContent());
    }
}
