package de.dasshorty.collectyourteam.backend.user.permissions;

import org.springframework.security.core.GrantedAuthority;

public interface Permission extends GrantedAuthority {

    String description();

    String name();

    @Override
    default String getAuthority() {
        return name().toUpperCase();
    }
}
