package de.dasshorty.collectyourteam.backend.user;


import de.dasshorty.collectyourteam.backend.image.ImageDto;
import de.dasshorty.collectyourteam.backend.role.RoleDto;
import de.dasshorty.collectyourteam.backend.role.permissions.AssignedPermission;
import de.dasshorty.collectyourteam.backend.role.permissions.Permission;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
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
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private List<AssignedPermission> grantedPermissions = new ArrayList<>();
    private List<AssignedPermission> deniedPermissions = new ArrayList<>();

    @DocumentReference
    private ImageDto profileImage;

    @DocumentReference
    private List<RoleDto> roles = new ArrayList<>();

    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<Permission> permissions = new ArrayList<>();

        if (roles != null) {
            roles.sort((o1, o2) -> o1.getPermission().groupWeight() > o2.getPermission().groupWeight() ? 1 : -1);
            for (RoleDto role : roles) {

                if (role == null) {
                    continue;
                }

                permissions.addAll(role.getPermission().allowed());
                permissions.removeAll(role.getPermission().denied());
            }
        }

        permissions.addAll(grantedPermissions.stream().map(AssignedPermission::permission).toList());
        permissions.removeAll(deniedPermissions.stream().map(AssignedPermission::permission).toList());

        return permissions;
    }
}
