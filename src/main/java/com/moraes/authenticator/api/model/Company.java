package com.moraes.authenticator.api.model;

import java.util.List;

import com.moraes.authenticator.api.model.menu.Question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "company", schema = "authenticator")
public class Company {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    private boolean enabled;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Question> questions;

    public Company(Long key) {
        this.key = key;
    }

    @PrePersist
    public void prePersist() {
        enabled = true;
    }
}
