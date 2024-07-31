package de.dasshorty.collectyourteam.backend.role;

import de.dasshorty.collectyourteam.backend.role.permissions.GroupedPermission;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Getter
@Setter
public class RoleDto {

    @Id
    private ObjectId id;

    private String name;
    private String hexColor;
    private boolean deletable;

    private GroupedPermission permission;

}
