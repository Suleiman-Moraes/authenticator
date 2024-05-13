package com.moraes.authenticator.api.model.real_state;

import com.moraes.authenticator.api.model.abstracts.AbstractSimpleAuditingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "construction", schema = "real_state")
public class Construction extends AbstractSimpleAuditingEntity {

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    private boolean enabled;
}
