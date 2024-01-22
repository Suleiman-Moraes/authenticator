package com.moraes.authenticator.api.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.model.abstracts.AbstractAuditingEntity;
import com.moraes.authenticator.api.model.dto.PermissionDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.interfaces.IModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Audited
@Entity
@Table(name = "users", schema = "authenticator")
public class User extends AbstractAuditingEntity implements IModel<Long>, UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(unique = true, nullable = false, length = 150)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "token_reset_password", length = 36, unique = true)
    private UUID tokenResetPassword;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    private boolean enabled;

    @Column(name = "token_reset_password_enabled", nullable = false)
    private boolean tokenResetPasswordEnabled;

    @Column(name = "token_reset_password_expiration_date")
    private LocalDateTime tokenResetPasswordExpirationDate;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profile")
    private Profile profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_person")
    private Person person;

    @Transient
    public List<String> getRoles() {
        return CollectionUtils.isEmpty(getAuthorities()) ? new LinkedList<>()
                : getAuthorities().stream()
                        .map(a -> a.getRole().getRoleUnderline())
                        .toList();
    }

    @Transient
    @Override
    public Collection<PermissionDTO> getAuthorities() {
        return profile != null ? RoleEnum.generate(profile.getRoles()) : new LinkedList<>();
    }

    @PrePersist
    public void prePersist() {
        tokenResetPasswordEnabled = false;
    }
}
