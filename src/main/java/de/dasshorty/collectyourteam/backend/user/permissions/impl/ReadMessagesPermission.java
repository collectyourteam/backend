package de.dasshorty.collectyourteam.backend.user.permissions.impl;

import de.dasshorty.collectyourteam.backend.user.permissions.Permission;

public class ReadMessagesPermission implements Permission {
    @Override
    public String description() {
        return "The user can read messages from other users";
    }

    @Override
    public String name() {
        return "READ_MESSAGES";
    }
}
