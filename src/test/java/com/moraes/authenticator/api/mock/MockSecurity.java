package com.moraes.authenticator.api.mock;

import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.enums.RoleEnum;

public class MockSecurity {
    
    public void mockSuperUser(){
        User user = new MockUser().mockEntity(1);
        user.setKey(1L);
        user.getProfile().setKey(2L);
        user.getCompany().setKey(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "test", user.getAuthorities()));
    }
    
    public void mockUserWithoutAuthorities(){
        User user = new MockUser().mockEntity(1);
        user.setKey(1L);
        user.getProfile().setKey(2L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "test", null));
    }
    
    public void mockNullUser(){
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(null, null));
    }
    
    public void mockUserWithAuthorityParam(RoleEnum roleEnum){
        User user = new MockUser().mockEntity(1);
        user.setKey(1L);
        user.getProfile().setKey(2L);
        user.getProfile().setRoles(Set.of(roleEnum));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "test", null));
    }
}
