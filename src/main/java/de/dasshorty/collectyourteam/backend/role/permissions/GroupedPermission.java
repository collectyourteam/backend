package de.dasshorty.collectyourteam.backend.role.permissions;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public record GroupedPermission(ObjectId roleId, ArrayList<Permission> allowed,
                                ArrayList<Permission> denied,
                                int groupWeight) {
}
