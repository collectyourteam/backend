package de.dasshorty.collectyourteam.backend.role.permissions;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
    ADMIN("Administrator", "All privileges are granted to the user"),
    MANAGE_SERVER("Manage server", "User can manage your server"),
    READ_MESSAGES("Read Messages", "User can read messages"),
    SEND_MESSAGES("Send Messages", "User can send messages");

    private final String roleName;
    private final String description;

    Permission(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return this.name().toUpperCase();
    }
}
