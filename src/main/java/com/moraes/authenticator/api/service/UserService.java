package com.moraes.authenticator.api.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.ExceptionUtilDTO;
import com.moraes.authenticator.api.model.dto.user.UserDTO;
import com.moraes.authenticator.api.model.dto.user.UserEnabledDTO;
import com.moraes.authenticator.api.model.dto.user.UserMeDTO;
import com.moraes.authenticator.api.model.dto.user.UserNewPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordDTO;
import com.moraes.authenticator.api.model.enums.ParamEnum;
import com.moraes.authenticator.api.repository.IUserRepository;
import com.moraes.authenticator.api.service.interfaces.IInformationSenderService;
import com.moraes.authenticator.api.service.interfaces.IParamService;
import com.moraes.authenticator.api.service.interfaces.IProfileService;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.api.util.ConstantsUtil;
import com.moraes.authenticator.api.util.ExceptionsUtil;
import com.moraes.authenticator.api.util.MessagesUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    @Getter
    private IUserRepository repository;

    private PasswordEncoder passwordEncoder;

    private IProfileService profileService;

    private IParamService paramService;

    private IInformationSenderService informationSenderService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(MessagesUtil.getMessage("user_not_found")));
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Transactional
    @Override
    public void update(UserDTO object, Long key) {
        valid(key, object.getProfile().getKey(), object.getUsername());
        User entity = findByKey(key);
        entity.setUsername(object.getUsername());
        entity.setProfile(Profile.builder().key(object.getProfile().getKey()).build());
        save(entity);
    }

    @Transactional
    @Override
    public void updateMe(UserMeDTO object, Long key) {
        validMe(key, object.getUsername());
        User entity = findByKey(key);
        entity.setUsername(object.getUsername());
        save(entity);
    }

    @Transactional
    @Override
    public Long insert(User entity, Long personKey) {
        entity.setPerson(Person.builder().key(personKey).build());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setEnabled(true);
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        save(entity);
        return entity.getKey();
    }

    @Override
    public User getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public void validInsert(User entity) {
        if (!StringUtils.hasText(entity.getPassword())) {
            throw new ValidException(
                    MessagesUtil.getMessage(MessagesUtil.NOT_BLANK, MessagesUtil.getMessage("user.password")));
        }
        valid(entity.getKey(), entity.getProfile().getKey(), entity.getUsername());
    }

    @Override
    public void delete(Long key) {
        // User one is root
        ExceptionsUtil.throwValidExceptions(
                ExceptionUtilDTO.builder()
                        .condition(ConstantsUtil.ONE.longValue() != key)
                        .messageKey("user.not.can.deleted")
                        .build());
        getRepository().delete(findByKey(key));
    }

    @Override
    public User preInsertMe(User user) {
        user.setProfile(new Profile());
        profileService.preInsertMe(user.getProfile());
        return user;
    }

    @Override
    public User updateEnabled(UserEnabledDTO entity, long key) {
        User user = findByKey(key);
        return updateEnabled(entity, user);
    }

    @Override
    public User updateDisabledMe() {
        User user = getMe();
        return updateEnabled(new UserEnabledDTO(false), user);
    }

    @Override
    public void changePasswordMe(UserNewPasswordDTO dto) {
        User user = repository.findByUsername(getMe().getUsername()).orElseThrow(ResourceNotFoundException::new);
        verifyPassword(user, dto.oldPassword());
        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        save(user);
    }

    @Override
    public void resetPassword(UserResetPasswordDTO userResetPasswordDTO) {
        User user = repository.findByUsernameAndPersonEmail(userResetPasswordDTO.username(),
                userResetPasswordDTO.email()).orElseThrow(ResourceNotFoundException::new);
        final String timeExpiration = paramService
                .findByNameIfNotExistsCreate(ParamEnum.TOKEN_RESET_PASSWORD_EXPIRATION_TIME).getValue();

        user.setTokenResetPasswordEnabled(true);
        user.setTokenResetPassword(UUID.randomUUID());
        user.setTokenResetPasswordExpirationDate(LocalDateTime.now().plusHours(Long.valueOf(timeExpiration)));
        save(user);
        informationSenderService.sendEmailResetPassword(user, timeExpiration,
                paramService.findByNameIfNotExistsCreate(ParamEnum.URL_FRONT_END));
    }

    /**
     * Only pass fields not objects
     * as you can use with Entity and DTO
     * 
     * @param key
     * @param profileKey
     * @param username
     */
    public void valid(Long key, Long profileKey, String username) {
        key = key != null ? key : 0;
        ExceptionsUtil.throwValidExceptions(
                getValidUsernameUnique(key, username),
                ExceptionUtilDTO.builder()
                        .condition(ConstantsUtil.ONE.longValue() != profileKey
                                || ConstantsUtil.ONE.longValue() == key)
                        .messageKey("user.profile.unavailable")
                        .build());
    }

    /**
     * Only pass fields not objects
     * as you can use with Entity and DTO
     * 
     * @param key
     * @param username
     */
    public void validMe(Long key, String username) {
        key = key != null ? key : 0;
        ExceptionsUtil.throwValidExceptions(
                getValidUsernameUnique(key, username));
    }

    /**
     * This code snippet is a private method called verifyPassword in a Java class.
     * It takes in a User object and a String password as parameters. The method
     * uses a passwordEncoder to compare the input password with the stored password
     * of the user. If the passwords do not match, it throws a ValidException with a
     * specific error message.
     *
     * @param user     the user whose password needs to be verified
     * @param password the password to be verified
     * @return nothing
     */
    public void verifyPassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidException(
                    MessagesUtil.getMessage(MessagesUtil.getMessage("user.password-invalid")));
        }
    }

    private ExceptionUtilDTO getValidUsernameUnique(Long key, String username) {
        return ExceptionUtilDTO.builder()
                .condition(!repository.existsByUsernameAndKeyNot(username,
                        key))
                .messageKey("user.username.unique")
                .build();
    }

    private void save(User entity) {
        repository.save(entity);
    }

    private User updateEnabled(UserEnabledDTO entity, User user) {
        user.setEnabled(entity.enabled());
        save(user);
        return user;
    }
}
