package de.dasshorty.collectyourteam.backend.role.body;

import de.dasshorty.collectyourteam.backend.role.permissions.GroupedPermission;

public record RoleBody(String id, String name, String hexColor, boolean deletable, GroupedPermission permission) {
}
