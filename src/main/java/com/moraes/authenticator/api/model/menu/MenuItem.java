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
import jakarta.persistence.ManyToOne;
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
@Table(name = "menu_item", schema = "menu")
public class MenuItem {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(nullable = false, length = 50)
    private String label;

    @Column(nullable = false, length = 50)
    private String icon;

    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_menu")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_menu_item")
    private MenuItem itemParent;

    @ElementCollection
    @CollectionTable(name = "menu_item_role", joinColumns = @JoinColumn(name = "id_menu_item"), schema = "menu")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private List<RoleEnum> roles;

    @ElementCollection
    @CollectionTable(name = "menu_item_router_link", joinColumns = @JoinColumn(name = "id_menu_item"), schema = "menu")
    @Column(name = "comand")
    private List<String> routerLink;

    @OneToMany(mappedBy = "itemParent", fetch = FetchType.LAZY)
    private List<MenuItem> items;

    @PrePersist
    public void prePersist() {
        enabled = enabled == null ? Boolean.TRUE : enabled;
    }
}
