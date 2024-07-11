package com.moraes.authenticator.api.model.real_state;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.moraes.authenticator.api.model.abstracts.AbstractSimpleAuditingEntity;

import jakarta.persistence.*;
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
@Table(name = "proposal", schema = "real_state")
public class Proposal extends AbstractSimpleAuditingEntity {

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
    @JoinColumn(name = "id_enterprise")
    private Enterprise enterprise;

    @OrderBy("key ASC")
    @OneToMany(mappedBy = "proposal", fetch = FetchType.LAZY)
    private List<Condition> conditions;

    @Override
    protected void prePersistOthers() {
        date = LocalDateTime.now();
        enabled = true;
    }
}
