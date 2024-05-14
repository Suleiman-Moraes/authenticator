package com.moraes.authenticator.api.model.real_state;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.moraes.authenticator.api.model.abstracts.AbstractSimpleAuditingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "enterprise", schema = "real_state")
public class Enterprise extends AbstractSimpleAuditingEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private BigDecimal value;

    private BigDecimal vpl;

    @Column(nullable = false, name = "value_m2")
    private BigDecimal valueM2;

    @Column(nullable = false, name = "size_m2")
    private BigDecimal sizeM2;

    @Column(nullable = false)
    private LocalDateTime date;

    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_construction")
    private Construction construction;

    @Override
    protected void prePersistOthers() {
        date = LocalDateTime.now();
    }
}
