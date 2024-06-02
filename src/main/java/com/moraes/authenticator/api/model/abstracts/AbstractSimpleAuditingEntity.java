package com.moraes.authenticator.api.model.abstracts;

import java.time.LocalDateTime;
import java.util.Optional;

import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.interfaces.IModel;
import com.moraes.authenticator.api.util.SecurityUtil;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@MappedSuperclass
public abstract class AbstractSimpleAuditingEntity implements IModel<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    protected User createdBy;

    @Column(name = "created_date", updatable = false, nullable = false)
    protected LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    protected User lastModifiedBy;

    @Column(name = "last_modified_date", nullable = false)
    protected LocalDateTime lastModifiedDate;

    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
        Optional.ofNullable(SecurityUtil.getPrincipal())
                .ifPresentOrElse(user -> createdBy = User.builder().key(user.getKey()).build(), () -> createdBy = null);
        prePersistAndUpdate();
        prePersistOthers();
    }

    @PreUpdate
    public void preUpdate() {
        prePersistAndUpdate();
    }

    protected void prePersistAndUpdate() {
        lastModifiedDate = LocalDateTime.now();
        Optional.ofNullable(SecurityUtil.getPrincipal()).ifPresentOrElse(
                user -> lastModifiedBy = User.builder().key(user.getKey()).build(), () -> lastModifiedBy = null);
    }

    protected void prePersistOthers() {
        // Empty
    }
}
