package de.dasshorty.collectyourteam.backend.user.permissions.impl;

import de.dasshorty.collectyourteam.backend.user.permissions.Permission;

public class AdminPermission implements Permission {
    @Override
    public String description() {
        return "All privileges are granted to the user";
    }

    @Override
    public String name() {
        return "ADMIN";
    }
}
