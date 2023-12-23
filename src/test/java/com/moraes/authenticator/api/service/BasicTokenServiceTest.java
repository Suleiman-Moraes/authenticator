package com.moraes.authenticator.api.service;

import static com.moraes.authenticator.api.mock.MockBasicToken.PASSWORD_DECRYPTED;
import static com.moraes.authenticator.api.mock.MockBasicToken.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.moraes.authenticator.api.exception.UnauthorizedBasic;
import com.moraes.authenticator.api.mock.MockBasicToken;
import com.moraes.authenticator.api.model.BasicToken;
import com.moraes.authenticator.api.repository.IBasicTokenRepository;
import com.moraes.authenticator.api.util.ConstantsUtil;

import jakarta.servlet.http.HttpServletRequest;

class BasicTokenServiceTest {

    private MockBasicToken input;

    @Spy
    @InjectMocks
    private BasicTokenService service;

    @Mock
    private IBasicTokenRepository repository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final Long key = 1l;
    private BasicToken entity;

    @BeforeEach
    void setUp() {
        input = new MockBasicToken();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(key);
    }

    @Test
    void testFindAll() {
        final List<BasicToken> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        assertEquals(list, service.findAll(), "Return not equal");
    }

    @Test
    void testValidateBasicToken() {
        final String basicAuthorization = String.format("%s %s", ConstantsUtil.BASIC,
                input.getBasicTokenHash());

        assertThrows(UnauthorizedBasic.class, () -> service.validateBasicToken(), "Return not equal");

        when(request.getHeader(ConstantsUtil.AUTHORIZATION)).thenReturn(basicAuthorization);
        assertThrows(UnauthorizedBasic.class, () -> service.validateBasicToken(), "Return not equal");

        when(repository.findByUsername(USERNAME)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches(PASSWORD_DECRYPTED, entity.getPassword())).thenReturn(true);
        assertTrue(service.validateBasicToken(), "Return not equal");
    }
}
