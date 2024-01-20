package com.moraes.authenticator.api.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

    @Column(nullable = false)
    private String password;

    @Column(name = "account_non_expired")
    private boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private boolean credentialsNonExpired;

    private boolean enabled;

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
}
