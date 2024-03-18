package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.mock.MockParam;
import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.KeyDTO;
import com.moraes.authenticator.api.model.dto.user.UserDTO;
import com.moraes.authenticator.api.model.dto.user.UserEnabledDTO;
import com.moraes.authenticator.api.model.dto.user.UserNewPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordTokenDTO;
import com.moraes.authenticator.api.model.enums.ParamEnum;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.repository.IUserRepository;
import com.moraes.authenticator.api.service.interfaces.IInformationSenderService;
import com.moraes.authenticator.api.service.interfaces.IParamService;
import com.moraes.authenticator.api.service.interfaces.IProfileService;
import com.moraes.authenticator.api.util.MessagesUtil;

class UserServiceTest {

    private MockUser input;

    @Spy
    @InjectMocks
    private UserService service;

    @Mock
    private IUserRepository repository;

    @Mock
    private IProfileService profileService;

    @Mock
    private IParamService paramService;

    @Mock
    private IInformationSenderService informationSenderService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final Long key = 1l;
    private User entity;
    private MockParam mockParam;

    @BeforeEach
    void setUp() {
        input = new MockUser();
        mockParam = new MockParam();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(key);
        entity.getProfile().setKey(2L);
    }

    @Test
    void testExistsByUsername() {
        assertFalse(service.existsByUsername("Test"), "Return not equal");
    }

    @Test
    void testLoadUserByUsername() {
        when(repository.findByUsername(entity.getUsername())).thenReturn(Optional.of(entity));
        User ret = (User) service.loadUserByUsername(entity.getUsername());
        assertNotNull(ret, "Retunr null");
        assertEquals(entity, ret, "Return not equal");
    }

    @Test
    void testLoadUserByUsernameThrowUsernameNotFoundException() {
        final String username = "Test";
        when(repository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username), "Does Not Throw");
    }

    @Test
    void testUpdate() {
        User entity = input.mockEntity(2);
        entity.setKey(key);
        when(repository.existsByUsernameAndKeyNot(anyString(), anyLong())).thenReturn(false);
        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        UserDTO dto = input.mockUserDTO(2);
        assertDoesNotThrow(() -> service.update(dto, key), "Does Not Throw");
        assertNotNull(entity, "Return null");
        assertNotEquals(this.entity, entity, "Return equal");
    }

    @Test
    void testInsert() {
        mockAuthentication();
        when(repository.existsByUsernameAndKeyNot(anyString(), anyLong())).thenReturn(false);
        assertEquals(key, service.insert(entity, key), "Return not equal");
    }

    @Test
    @DisplayName("JUnit test Given a user and a personKey, when insertForAdmin is called, should return the user key")
    void testGivenUserAndPersonKeyWhenInsertForAdminThenReturnUserKey() {
        mockAuthentication();
        when(repository.existsByUsernameAndKeyNot(anyString(), anyLong())).thenReturn(false);
        assertEquals(key, service.insertForAdmin(entity, key), "Key is not equal");
    }

    @Test
    void testFindByKey() {
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        User ret = service.findByKey(key);
        assertNotNull(ret, "Retunr null");
        assertEquals(entity, ret, "Return not equal");
    }

    @Test
    void testFindByKeyThrowResourceNotFoundException() {
        when(repository.findById(key)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findByKey(key), "Does Not Throw");
    }

    @Test
    void testDelete() {
        when(repository.findById(2L)).thenReturn(Optional.of(entity));
        assertDoesNotThrow(() -> service.delete(2L), "Does Not Throw");
    }

    @Test
    void testDeleteThrow() {
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        assertThrows(ValidException.class, () -> service.delete(key), "Throw Exception");
    }

    @Test
    void testGetMe() {
        mockAuthentication();
        assertNotNull(service.getMe(), "Return not null");
    }

    @Test
    void testGetMeNull() {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(null, "",
                entity.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertNull(service.getMe(), "Return null");
    }

    @Test
    void testValidInsert() {
        final User user = new User();
        assertThrows(ValidException.class, () -> service.validInsert(user), "Does Not Throw - validInsert");
    }

    @Test
    void testValid() {
        when(repository.existsByUsernameAndKeyNot(anyString(), anyLong())).thenReturn(true);
        try {
            User entity = input.mockEntity(2);
            entity.getProfile().setKey(1L);
            service.valid(entity.getKey(), entity.getProfile().getKey(), entity.getUsername());
        } catch (ValidException e) {
            assertEquals(2, e.getErrs().size(), "Return not equal");
            assertEquals(MessagesUtil.getMessage("user.username.unique"), e.getErrs().get(0), "Return not equal");
            assertEquals(MessagesUtil.getMessage("user.profile.unavailable"), e.getErrs().get(1), "Return not equal");
        }
    }

    @Test
    void testPreInsertMe() {
        when(paramService.findByNameIfNotExistsCreate(ParamEnum.USER_DEFAULT_PROFILE_KEY))
                .thenReturn(mockParam.mockEntity(ParamEnum.USER_DEFAULT_PROFILE_KEY));
        User user = new User();
        user.setProfile(null);
        assertNotNull(service.preInsertMe(user).getProfile(), "Return not equal");
    }

   @Test
   void testUpdateMe() {
       mockAuthentication();
       User entity = input.mockEntity(2);
       entity.setKey(key);

       UserDTO dto = input.mockUserDTO(2);
       dto.setProfile(new KeyDTO(3L));

       ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

       assertDoesNotThrow(() -> service.updateMe(dto, entity), "Does Not Throw");
       verify(service, times(1)).save(argumentCaptor.capture());
       assertNotNull(entity, "Return null");
       assertNotEquals(this.entity, entity, "Return equal");
       assertEquals(dto.getProfile().getKey(), argumentCaptor.getValue().getProfile().getKey(), "Return not equal");
   }

   @Test
   @DisplayName("JUnit test Given a user DTO and a user, when updateMe is called with not admin user, should return the user key")
   void testGivenUserDtoAndUserWhenUpdateMeWithNotAdminUserThenReturnUserKey() {
       User entity = input.mockEntity(2);
       entity.getProfile().setRoles(Set.of(RoleEnum.COMMON_USER));
       entity.getProfile().setKey(3L);

       final Authentication authentication = new UsernamePasswordAuthenticationToken(entity, "",
               entity.getAuthorities());
       SecurityContextHolder.getContext().setAuthentication(authentication);

       entity.setKey(key);
       UserDTO dto = input.mockUserDTO(2);
       dto.setProfile(new KeyDTO(2L));

       ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);

       assertDoesNotThrow(() -> service.updateMe(dto, entity), "Does Not Throw");
       verify(service, times(1)).save(argumentCaptor.capture());
       assertNotNull(entity, "Return null");
       assertNotEquals(this.entity, entity, "Return equal");
       assertNotEquals(dto.getProfile().getKey(), argumentCaptor.getValue().getProfile().getKey(), "Return equal");
   }

   @Test
   @DisplayName("JUnit test Given a user DTO with root profile and a user entity, when updateMe is called, should throw ValidException")
   void testGivenUserDtoWithRootProfileAndUserEntityWhenUpdateMeThenThrowValidException() {
       mockAuthentication();
       final Long key = 2L;
       User entity = input.mockEntity(2);
       entity.setKey(key);
       UserDTO dto = input.mockUserDTO(2);
       dto.setProfile(new KeyDTO(1L));

       assertThrows(ValidException.class, () -> service.updateMe(dto, entity), "Does Not Throw");
   }

    @Test
    void testValidMe() {
        when(repository.existsByUsernameAndKeyNot(anyString(), anyLong())).thenReturn(false);
        try {
            final User entity = input.mockEntity(2);
            service.validMe(entity.getKey(), entity.getUsername());
        } catch (ValidException e) {
            assertEquals(1, e.getErrs().size(), "Return not equal");
            assertEquals(MessagesUtil.getMessage("user.username.unique"), e.getErrs().get(0), "Return not equal");
        }
    }

    @Test
    void testUpdateEnabled() {
        when(repository.findByKeyAndCompanyKey(eq(key), any())).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        assertFalse(service.updateEnabled(new UserEnabledDTO(false), key).isEnabled(), "Return not false");
    }

    @Test
    void testUpdateDisabledMe() {
        mockAuthentication();
        when(repository.save(any())).thenReturn(entity);
        assertFalse(service.updateDisabledMe().isEnabled(), "Return not false");
    }

    @Test
    void changePasswordMe_ValidOldPassword_UpdatePassword() {
        // Arrange
        final UserNewPasswordDTO dto = input.mockUserNewPasswordDTO();
        entity.setPassword("oldEncodedPassword");
        mockAuthentication();
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(entity));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");

        // Act
        service.changePasswordMe(dto);

        // Assert
        assertEquals("newEncodedPassword", entity.getPassword());
        verify(repository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(repository, times(1)).save(entity);
    }

    @Test
    void changePasswordMe_InvalidOldPassword_ThrowException() {
        // Arrange
        final UserNewPasswordDTO dto = input.mockUserNewPasswordDTO();
        mockAuthentication();
        when(repository.findByUsername(anyString())).thenReturn(Optional.of(entity));

        // Act & Assert
        assertThrows(ValidException.class, () -> service.changePasswordMe(dto));
        verify(repository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(entity);
    }

    @Test
    void changePasswordMe_UserNotFound_ThrowException() {
        // Arrange
        final UserNewPasswordDTO dto = input.mockUserNewPasswordDTO();
        mockAuthentication();
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.changePasswordMe(dto));
        verify(repository, times(1)).findByUsername(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("JUnit test Given a user and a invalid password, when verifyPassword is called, then it throws an exception")
    void testGivenAUserAndAInvalidPasswordWhenVerifyPasswordIsCalledThenItThrowsAnException() {
        assertThrows(ValidException.class, () -> service.verifyPassword(entity, "invalidPassword"), "Throw Exception");
    }

    @Test
    @DisplayName("JUnit test Given a UserResetPasswordDTO, when resetPassword is called, then generate a new token")
    void testGivenAUserResetPasswordDTOWhenResetPasswordIsCalledThenGenerateANewToken() {
        // Arrange
        UserResetPasswordDTO userResetPasswordDTO = new UserResetPasswordDTO("username", "email@email.com");
        User user = new User();
        when(repository.findByUsernameAndPersonEmailAndEnabled(anyString(), anyString(), anyBoolean()))
                .thenReturn(Optional.of(user));
        when(paramService.findByNameIfNotExistsCreate(ParamEnum.TOKEN_RESET_PASSWORD_EXPIRATION_TIME))
                .thenReturn(mockParam.mockEntity(ParamEnum.TOKEN_RESET_PASSWORD_EXPIRATION_TIME));

        // Act
        service.resetPassword(userResetPasswordDTO);

        // Assert
        assertTrue(user.isTokenResetPasswordEnabled());
        assertNotNull(user.getTokenResetPassword());
        assertNotNull(user.getTokenResetPasswordExpirationDate());
        verify(repository).save(user);
    }

    @Test
    void resetPassword_validTokenAndEnabledAndNotExpired_resetPasswordSuccessfully() {
        // Arrange
        final UserResetPasswordTokenDTO userResetPasswordTokenDTO = input.mockUserResetPasswordTokenDTO();
        final String newEncodedPassword = "newEncodedPassword";
        User user = input.mockEntity(1);
        user.setTokenResetPasswordEnabled(true);
        user.setEnabled(true);
        user.setTokenResetPasswordExpirationDate(LocalDateTime.now().plusHours(1));
        when(passwordEncoder.encode(userResetPasswordTokenDTO.password())).thenReturn(newEncodedPassword);
        when(repository
                .findByTokenResetPasswordAndTokenResetPasswordEnabledAndEnabledAndTokenResetPasswordExpirationDateAfter(
                        eq(userResetPasswordTokenDTO.token()), eq(true), eq(true), any(LocalDateTime.class)))
                .thenReturn(Optional.of(user));

        // Act
        service.resetPassword(userResetPasswordTokenDTO);

        // Assert
        assertFalse(user.isTokenResetPasswordEnabled());
        verify(passwordEncoder, times(1)).encode(userResetPasswordTokenDTO.password());
        verify(repository, times(1)).save(user);
    }

    @Test
    void resetPassword_invalidToken_throwResourceNotFoundException() {
        final UserResetPasswordTokenDTO userResetPasswordTokenDTO = input.mockUserResetPasswordTokenDTO();
        // Arrange
        when(repository
                .findByTokenResetPasswordAndTokenResetPasswordEnabledAndEnabledAndTokenResetPasswordExpirationDateAfter(
                        eq(userResetPasswordTokenDTO.token()), eq(true), eq(true), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // Act
        assertThrows(ResourceNotFoundException.class, () -> service.resetPassword(userResetPasswordTokenDTO),
                "Should throw ResourceNotFoundException");

        // Assert
        // ResourceNotFoundException is expected
    }

    private void mockAuthentication() {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(entity, "",
                entity.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
