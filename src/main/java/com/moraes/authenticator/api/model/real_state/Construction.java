package com.moraes.authenticator.api.model.real_state;

import java.util.List;

import com.moraes.authenticator.api.model.Company;
import com.moraes.authenticator.api.model.abstracts.AbstractSimpleAuditingEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company")
    private Company company;

    @OneToMany(mappedBy = "construction", fetch = FetchType.LAZY)
    private List<Enterprise> enterprises;

    @Override
    protected void prePersistOthers() {
        enabled = true;
    }
}
