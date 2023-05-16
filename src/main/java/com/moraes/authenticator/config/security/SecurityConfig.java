package com.moraes.authenticator.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;
import org.springframework.security.web.SecurityFilterChain;

import com.moraes.authenticator.api.util.ConstantsUtil;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private JwtTokenProvider jwtTokenProvider;

    private static final String PBKDF2 = "pbkdf2";
    private static final int ITERATIONS = 185_000;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final String[] patterns = { "/auth/signin", "/auth/refresh/**", "/swagger-ui/**", "/v3/api-docs/**" };
        return http
                .httpBasic(basic -> basic.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(patterns)
                                .permitAll().requestMatchers("/api/v1/**")
                                .authenticated().requestMatchers("/users").denyAll())
                .cors(withDefaults())
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .build();
    }

    @Bean
    AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", ConstantsUtil.EIGHT, ITERATIONS,
                SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        encoders.put(PBKDF2, pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(PBKDF2, encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder;
    }
}
