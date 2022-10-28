package br.com.moraes.authenticator.api.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class Model implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column(name = "date_registration", nullable = false, updatable = false)
	protected Date dateRegistration;

	@Column(name = "date_update", nullable = false)
	protected Date dateUpdate;

	protected Model(Long id) {
		this.id = id;
	}

	@PrePersist
	public void prePersist() {
		dateRegistration = new Date();
		prePersistAndUpdate();
	}

	@PreUpdate
	public void preUpdate() {
		prePersistAndUpdate();
	}

	protected void prePersistAndUpdate() {
		dateUpdate = dateUpdate == null ? new Date() : dateUpdate;
	}
}