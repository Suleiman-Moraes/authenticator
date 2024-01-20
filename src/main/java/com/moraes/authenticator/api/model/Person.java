package com.moraes.authenticator.api.model;

import java.io.Serializable;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.moraes.authenticator.api.model.interfaces.IModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@Entity
@Table(name = "person", schema = "authenticator")
public class Person implements Serializable, IModel<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 255)
    private String address;

    @NotAudited
    @OneToOne(mappedBy = "person", fetch = FetchType.LAZY)
    private User user;
}
