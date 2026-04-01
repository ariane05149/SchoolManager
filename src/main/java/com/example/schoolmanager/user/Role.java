package com.example.schoolmanager.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public enum Role {
    ADMIN, STUDENT;

    public Collection<? extends GrantedAuthority> toAuthority() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.name()));
    }
}