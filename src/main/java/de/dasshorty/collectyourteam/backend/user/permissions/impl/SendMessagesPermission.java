package de.dasshorty.collectyourteam.backend.user.permissions.impl;

import de.dasshorty.collectyourteam.backend.user.permissions.Permission;

public class SendMessagesPermission implements Permission {
    @Override
    public String description() {
        return "The user can send messages to other users";
    }

    @Override
    public String name() {
        return "SEND_MESSAGES";
    }
}
