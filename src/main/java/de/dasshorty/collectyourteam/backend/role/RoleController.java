package de.dasshorty.collectyourteam.backend.role;

import de.dasshorty.collectyourteam.backend.role.body.RoleBody;
import de.dasshorty.collectyourteam.backend.role.permissions.GroupedPermission;
import de.dasshorty.collectyourteam.backend.role.permissions.Permission;
import de.dasshorty.collectyourteam.backend.user.UserDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasAnyRole('MANAGE_SERVER', 'ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {

        if (this.roleRepository.findById(roleDto.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("Role already exists");
        }

        RoleDto save = this.roleRepository.insert(roleDto);
        return ResponseEntity.ok(new RoleBody(save.getId().toHexString(),
                save.getName(),
                save.getHexColor(),
                save.isDeletable(),
                save.getPermission()));

    }

    @PreAuthorize("hasAnyRole('MANAGE_SERVER', 'ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateRole(@RequestBody RoleDto roleDto) {

        RoleDto save = this.roleRepository.save(roleDto);
        return ResponseEntity.ok(new RoleBody(save.getId().toHexString(),
                save.getName(),
                save.getHexColor(),
                save.isDeletable(),
                save.getPermission()));

    }

    @PreAuthorize("hasAnyRole('MANAGE_SERVER', 'ADMIN')")
    @PutMapping("/{id}/permission/add")
    public ResponseEntity<?> addPermission(@RequestParam Permission permission, @PathVariable("id") ObjectId roleId, @RequestParam boolean allowed) {

        Optional<RoleDto> optional = this.roleRepository.findById(roleId);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RoleDto roleDto = optional.get();

        if (roleDto.getPermission() == null) {

            roleDto.setPermission(new GroupedPermission(roleId, new ArrayList<>(), new ArrayList<>(), 1));

        }

        if (allowed) {

            roleDto.getPermission().allowed().add(permission);

        } else {

            roleDto.getPermission().denied().add(permission);

        }

        RoleDto save = this.roleRepository.save(roleDto);
        return ResponseEntity.ok(new RoleBody(save.getId().toHexString(),
                save.getName(),
                save.getHexColor(),
                save.isDeletable(),
                save.getPermission()));
    }

    @PreAuthorize("hasAnyRole('MANAGE_SERVER', 'ADMIN')")
    @PutMapping("/{id}/permission/remove")
    public ResponseEntity<?> removePermission(@RequestParam Permission permission, @PathVariable("id") ObjectId roleId, @RequestParam boolean allowed) {

        Optional<RoleDto> optional = this.roleRepository.findById(roleId);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RoleDto roleDto = optional.get();

        if (roleDto.getPermission() == null) {

            roleDto.setPermission(new GroupedPermission(roleId, new ArrayList<>(), new ArrayList<>(), 1));

        }

        if (allowed) {

            roleDto.getPermission().allowed().remove(permission);

        } else {

            roleDto.getPermission().denied().remove(permission);

        }

        RoleDto save = this.roleRepository.save(roleDto);
        return ResponseEntity.ok(new RoleBody(save.getId().toHexString(),
                save.getName(),
                save.getHexColor(),
                save.isDeletable(),
                save.getPermission()));
    }

    @PreAuthorize("hasAnyRole('MANAGE_SERVER', 'ADMIN')")
    @GetMapping("")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(this.roleRepository.findAll().stream().map(roleDto ->
                new RoleBody(roleDto.getId().toHexString(),
                        roleDto.getName(),
                        roleDto.getHexColor(),
                        roleDto.isDeletable(),
                        roleDto.getPermission())).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoles(@PathVariable("id") ObjectId id, @AuthenticationPrincipal UserDto userDto) {

        Optional<RoleDto> optional = userDto.getRoles().stream().filter(roleDto -> roleDto.getId().equals(id)).findFirst();

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.map(roleDto -> new RoleBody(roleDto.getId().toHexString(),
                    roleDto.getName(),
                    roleDto.getHexColor(),
                    roleDto.isDeletable(),
                    roleDto.getPermission())).get());
        } else {
            return ResponseEntity.badRequest().body("You are not allowed to view this role");
        }

    }
}
