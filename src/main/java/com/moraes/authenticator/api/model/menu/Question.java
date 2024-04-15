package com.moraes.authenticator.api.model.menu;

import java.io.Serializable;
import java.util.List;

import com.moraes.authenticator.api.model.Company;
import com.moraes.authenticator.api.model.enums.TypeEnum;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.model.interfaces.IModel;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "question", schema = "menu", uniqueConstraints = {
        @UniqueConstraint(name = "unique_question_type_from_value", columnNames = { "type_from", "value", "id_company" }),
        @UniqueConstraint(name = "unique_question_type_from_order", columnNames = { "type_from", "order", "id_company" })
})
public class Question implements Serializable, IModel<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(nullable = false, length = 255)
    private String value;

    @Column(length = 50)
    private String mask;

    @Column(name = "order_question", nullable = false)
    private Integer order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, name = "type_from")
    private TypeFromEnum typeFrom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TypeEnum type;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean required = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers;
}
