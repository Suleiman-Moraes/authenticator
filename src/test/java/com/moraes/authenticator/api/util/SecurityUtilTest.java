package com.moraes.authenticator.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.moraes.authenticator.api.exception.UnauthorizedBasic;
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.model.enums.RoleEnum;

class SecurityUtilTest {

    private MockSecurity mockSecurity;

    @BeforeEach
    void setUp() {
        mockSecurity = new MockSecurity();
    }

    @Test
    @DisplayName("Junit test given context when hasRole then return true")
    void givenContextWhenHasRoleThenReturnTrue() {
        mockSecurity.mockSuperUser();
        assertTrue(SecurityUtil.hasRole(RoleEnum.ADMIN), "Return is false");
    }

    @Test
    @DisplayName("Junit test given context when hasRole then return false")
    void givenContextWhenHasRoleThenReturnFalse() {
        mockSecurity.mockSuperUser();
        assertFalse(SecurityUtil.hasRole(RoleEnum.COMMON_USER), "Return is True");
    }

    @Test
    @DisplayName("Junit test given context when hasRoleAdmin then return true")
    void givenContextWhenHasRoleAdminThenReturnTrue() {
        mockSecurity.mockSuperUser();
        assertTrue(SecurityUtil.hasRoleAdmin(), "Return is false");
    }

    @Test
    @DisplayName("Junit test given context when hasRoleAdmin then return false")
    void givenContextWhenHasRoleAdminThenReturnFalse() {
        mockSecurity.mockUserWithAuthorityParam(RoleEnum.COMMON_USER);
        assertFalse(SecurityUtil.hasRoleAdmin(), "Return is True");
    }

    @Test
    @DisplayName("Junit test given context when getRoles then return list of roles")
    void givenContextWhenGetRolesThenReturnListOfRoles() {
        mockSecurity.mockSuperUser();
        assertEquals(1, SecurityUtil.getRoles().size(), "List size is different from 1");
    }

    @Test
    @DisplayName("Junit test given null context when getRoles then return empty list of roles")
    void givenNullContextWhenGetRolesThenReturnEmptyListOfRoles() {
        mockSecurity.mockNullUser();
        assertEquals(0, SecurityUtil.getRoles().size(), "List size is different from 0");
    }

    @Test
    void testGetPrincipal() {
        mockSecurity.mockSuperUser();
        assertNotNull(SecurityUtil.getPrincipal(), "Return not null");
    }

    @Test
    void testGetPrincipalNull() {
        mockSecurity.mockNullUser();
        assertNull(SecurityUtil.getPrincipal(), "Return null");
    }

    @Test
    @DisplayName("Junit test given null context when getPrincipalOrThrow then throw UnauthorizedBasic")
    void givenNullContextWhenGetPrincipalOrThrowThenThrowUnauthorizedBasic() {
        mockSecurity.mockNullUser();
        assertThrows(UnauthorizedBasic.class, SecurityUtil::getPrincipalOrThrow, "Throw UnauthorizedBasic");
    }
    
    @Test
    @DisplayName("Junit test given context when getPrincipalOrThrow then return User")
    void givenContextWhenGetPrincipalOrThrowThenReturnUser() {
        mockSecurity.mockSuperUser();
        assertNotNull(SecurityUtil.getPrincipalOrThrow(), "Return not null");
    }
}
