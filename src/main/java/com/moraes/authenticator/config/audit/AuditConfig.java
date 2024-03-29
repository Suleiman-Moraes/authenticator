package com.moraes.authenticator.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditingService")
public class AuditConfig {

    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditingService();
    }
}
