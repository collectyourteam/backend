package de.dasshorty.collectyourteam.backend.user.permissions;

import java.util.List;

public record GroupedPermission(String groupName,
                                String hexColor,
                                List<Permission> allowed,
                                List<Permission> denied,
                                int groupWeight) {
}
