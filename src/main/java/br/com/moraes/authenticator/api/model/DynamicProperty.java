package br.com.moraes.authenticator.api.model;

import br.com.moraes.authenticator.api.enums.DynamicPropertyEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "dynamic_property")
public class DynamicProperty {

	@Id
	@Enumerated(EnumType.STRING)
	@Column(name = "id_key")
	private DynamicPropertyEnum key;

	@Column(name = "value_property")
	private String value;
}
