package br.com.moraes.authenticator.api.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class Profile implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean list = true;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<User> users;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "profile_permission", joinColumns = { @JoinColumn(name = "id_profile") }, inverseJoinColumns = {
            @JoinColumn(name = "id_permission") })
    private List<Permission> authorities;
}