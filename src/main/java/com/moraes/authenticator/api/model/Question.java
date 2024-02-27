package com.moraes.authenticator.api.model;

import com.moraes.authenticator.api.model.enums.TypeEnum;
import com.moraes.authenticator.api.model.enums.TypeFieldEnum;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "question", schema = "menu")
public class Question {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(nullable = false, length = 255)
    private String value;

    @Column(nullable = false)
    private Integer order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, name = "type_from")
    private TypeFromEnum typeFrom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, name = "type_field")
    private TypeFieldEnum typeField;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TypeEnum type;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean required = false;
}
