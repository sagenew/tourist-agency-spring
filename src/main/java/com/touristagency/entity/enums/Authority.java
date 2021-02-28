package com.touristagency.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    USER, MANAGER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}