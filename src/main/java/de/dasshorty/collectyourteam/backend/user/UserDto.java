package de.dasshorty.collectyourteam.backend.user;


import de.dasshorty.collectyourteam.backend.user.permissions.AssignedPermission;
import de.dasshorty.collectyourteam.backend.user.permissions.GroupedPermission;
import de.dasshorty.collectyourteam.backend.user.permissions.Permission;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
public class UserDto implements UserDetails {

    @Id
    private ObjectId id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private List<AssignedPermission> grantedPermissions;
    private List<AssignedPermission> deniedPermissions;
    private List<GroupedPermission> groups;


    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<Permission> permissions = new ArrayList<>();

        if (groups != null) {
            groups.sort((o1, o2) -> o1.groupWeight() > o2.groupWeight() ? 1 : -1);
            for (GroupedPermission group : groups) {
                permissions.addAll(group.allowed());
                permissions.removeAll(group.denied());
            }
        }

        return permissions;
    }
}
