package com.moraes.authenticator.api.model.real_state;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.moraes.authenticator.api.model.enums.FrequencyEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "condition", schema = "real_state")
public class Condition {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private FrequencyEnum frequency;

    @Column(nullable = false, name = "number_installments")
    private Integer numberInstallments;

    @Column(name = "value_installments")
    private BigDecimal valueInstallments;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, name = "beginning_installment")
    private LocalDate beginningInstallment;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proposal")
    private Proposal proposal;

    @PrePersist
    public void prePersist() {
        enabled = true;
    }
}
