package com.moraes.authenticator.config.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.model.User;

@Service
public class AuditingService implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof User) {
            return Optional.of(((User) authentication.getPrincipal()).getUsername());
        }
        return Optional.of("Uninformed");
    }
}