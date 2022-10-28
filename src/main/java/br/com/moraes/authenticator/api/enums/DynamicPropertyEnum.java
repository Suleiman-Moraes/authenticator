package br.com.moraes.authenticator.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DynamicPropertyEnum {
	CORS_ORIGIN_PATTERNS("http://localhost:3000,http://localhost:8080"), 
	SECRET_KEY("Duruod1rWwzrXsd3CdtXu9D6TFzZRpJn"), 
	EXPIRE_LENGTH("3600000");

	private String defaultValue;
}
