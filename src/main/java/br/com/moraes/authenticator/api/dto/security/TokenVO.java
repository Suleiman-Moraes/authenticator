package br.com.moraes.authenticator.api.dto.security;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	
	private String authenticated;
	
	private String accessToken;
	
	private String refreshToken;
	
	private Date created;
	
	private Date expiration;
}
