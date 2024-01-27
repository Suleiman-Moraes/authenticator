package com.moraes.authenticator.config.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moraes.authenticator.api.exception.JwtAuthenticationException;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.util.ConstantsUtil;
import com.moraes.authenticator.config.security.dto.TokenDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JwtTokenProvider {

    /**
     *
     */
    private static final String ROLES = "roles";

    private static final long ONE_HOUR = 3_600_000;

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = ONE_HOUR;

    private UserDetailsService userDetailsService;

    private Algorithm algorithm;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    @Transactional(readOnly = true)
    public TokenDTO createAccessToken(String username, List<String> roles) {
        final Date now = new Date();
        final Date validity = new Date(now.getTime() + validityInMilliseconds);
        return TokenDTO.builder()
                .username(username)
                .authenticated(Boolean.TRUE)
                .created(now)
                .expiration(validity)
                .accessToken(getAccessToken(username, roles, now, validity))
                .refreshToken(getRefreshToken(username, roles, now))
                .build();
    }

    @Transactional(readOnly = true)
    public Authentication getAuthentication(String token) {
        final DecodedJWT decodedJWT = decodedToken(token);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        final String bearer = ConstantsUtil.BEARER + " ";
        final String bearerToken = request.getHeader(ConstantsUtil.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(bearer)) {
            return bearerToken.substring(bearer.length());
        }
        return null;
    }

    public boolean validateToken(String token) throws JwtAuthenticationException {
        try {
            return !decodedToken(token).getExpiresAt().before(new Date());
        } catch (Exception e) {
            log.warn("validateToken " + e.getMessage(), e);
            throw new JwtAuthenticationException();
        }
    }

    @Transactional(readOnly = true)
    public TokenDTO refreshToken(String refreshToken) {
        final String bearer = ConstantsUtil.BEARER + " ";
        if (StringUtils.hasText(refreshToken) && refreshToken.contains(bearer)) {
            refreshToken = refreshToken.substring(bearer.length());
        }
        final DecodedJWT decodedJWT = decodedToken(refreshToken);
        final String username = decodedJWT.getSubject();
        final List<String> roles = decodedJWT.getClaim(ROLES).asList(String.class);
        return createAccessToken(username, roles);
    }

    @Transactional(readOnly = true)
    public String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        final Person person = ((User) userDetailsService.loadUserByUsername(username)).getPerson();
        final String name = person != null && StringUtils.hasText(person.getName()) ? person.getName() : username;
        return JWT.create().withClaim(ROLES, roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString())
                .withPayload(
                        Map.of("name", name))
                .sign(algorithm).strip();
    }

    private DecodedJWT decodedToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }

    private String getRefreshToken(String username, List<String> roles, Date now) {
        return JWT.create().withClaim(ROLES, roles)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + (validityInMilliseconds + ConstantsUtil.THREE)))
                .withSubject(username)
                .sign(algorithm).strip();
    }
}
