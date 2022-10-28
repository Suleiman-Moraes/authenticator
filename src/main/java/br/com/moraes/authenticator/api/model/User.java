package br.com.moraes.authenticator.api.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_tb")
public class User implements UserDetails, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50, unique = true)
	private String username;

	@Column(nullable = false, length = 150)
	private String password;

	@Builder.Default
	@Column
	private boolean enabled = true;

	@Builder.Default
	@Column(name = "credentials_non_expired")
	private boolean credentialsNonExpired = true;

	@Builder.Default
	@Column(name = "account_non_locked")
	private boolean accountNonLocked = true;

	@Builder.Default
	@Column(name = "account_non_expired")
	private boolean accountNonExpired = true;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_profile")
	private Profile profile;

	public User(Long id) {
		this.id = id;
	}

	@Transient
	@Override
	public Collection<Permission> getAuthorities() {
		return profile.getAuthorities();
	}

	@Transient
	public List<String> getRoles() {
		return CollectionUtils.isEmpty(getAuthorities()) ? new LinkedList<>()
				: getAuthorities().stream().map(auth -> auth.getAuthority()).toList();
	}
}