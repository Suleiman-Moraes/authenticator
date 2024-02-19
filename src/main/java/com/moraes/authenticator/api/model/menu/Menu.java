package com.moraes.authenticator.api.model.menu;

import java.util.List;

import com.moraes.authenticator.api.model.enums.RoleEnum;

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
@Table(name = "menu", schema = "menu")
public class Menu {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(nullable = false, length = 50)
    private String label;

    private Boolean enabled;

    @ElementCollection
    @CollectionTable(name = "menu_role", joinColumns = @JoinColumn(name = "id_menu"), schema = "menu")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<RoleEnum> roles;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuItem> items;

    @PrePersist
    public void prePersist() {
        enabled = enabled == null ? Boolean.TRUE : enabled;
    }
}
