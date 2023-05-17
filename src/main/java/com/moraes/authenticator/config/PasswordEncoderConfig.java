package com.moraes.authenticator.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm;

import com.moraes.authenticator.api.util.ConstantsUtil;

@Configuration
public class PasswordEncoderConfig {

    private static final String PBKDF2 = "pbkdf2";
    private static final int ITERATIONS = 185_000;
    
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
