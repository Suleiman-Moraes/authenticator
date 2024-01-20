package com.moraes.authenticator.api.model;

import com.moraes.authenticator.api.model.enums.ParamEnum;

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
@Table(name = "param", schema = "authenticator")
public class Param {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false, length = 50)
    private ParamEnum name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 50)
    private String value;
}
