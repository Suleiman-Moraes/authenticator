package com.moraes.authenticator.api.mock;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.moraes.authenticator.api.model.User;

public class MockSecurity {
    
    public void mockSuperUser(){
        User user = new MockUser().mockEntity(1);
        user.setKey(1L);
        user.getProfile().setKey(2L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
    }
}
