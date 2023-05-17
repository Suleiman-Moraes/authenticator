package com.moraes.authenticator.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.interfaces.IModel;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
@Table(name = "profile")
public class Profile implements Serializable, IModel<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(length = 150)
    private String description;

    @ElementCollection
    @CollectionTable(name = "profile_role", joinColumns = @JoinColumn(name = "id_profile"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<RoleEnum> roles;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<User> users;
}
