package de.dasshorty.collectyourteam.backend.util;

import de.dasshorty.collectyourteam.backend.image.ImageDto;
import de.dasshorty.collectyourteam.backend.image.ImageRepository;
import de.dasshorty.collectyourteam.backend.role.RoleDto;
import de.dasshorty.collectyourteam.backend.role.RoleRepository;
import de.dasshorty.collectyourteam.backend.role.permissions.AssignedPermission;
import de.dasshorty.collectyourteam.backend.role.permissions.GroupedPermission;
import de.dasshorty.collectyourteam.backend.role.permissions.Permission;
import de.dasshorty.collectyourteam.backend.server.ServerDto;
import de.dasshorty.collectyourteam.backend.server.ServerRepository;
import de.dasshorty.collectyourteam.backend.user.UserDto;
import de.dasshorty.collectyourteam.backend.user.UserRepository;
import lombok.val;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DataGenerator implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServerRepository serverRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public DataGenerator(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ServerRepository serverRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.serverRepository = serverRepository;
        this.imageRepository = imageRepository;
    }

    private byte[] downloadImage(String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        return inputStream.readAllBytes();
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.serverRepository.count() == 0) {

            ServerDto serverDto = new ServerDto();

            serverDto.setServerName("collectyour.team");
            serverDto.setServerDescription("Manage your Team Server");

            ImageDto banner = new ImageDto();
            banner.setImage(new Binary(this.downloadImage("https://static.collectyour.team/logos/big-logo.png")));
            serverDto.setBanner(this.imageRepository.save(banner));

            ImageDto logo = new ImageDto();
            logo.setImage(new Binary(this.downloadImage("https://static.collectyour.team/logos/logo-small-colored-bg.png")));
            serverDto.setLogo(this.imageRepository.save(logo));

            this.serverRepository.save(serverDto);
        }

        if (this.roleRepository.count() == 0) {

            RoleDto everyoneRole = new RoleDto();
            everyoneRole.setName("everyone");
            everyoneRole.setHexColor("#ffffff");
            everyoneRole.setDeletable(false);

            everyoneRole = this.roleRepository.save(everyoneRole);

            everyoneRole.setPermission(new GroupedPermission(everyoneRole.getId(), new ArrayList<>(List.of(Permission.READ_MESSAGES)),
                    new ArrayList<>(List.of(Permission.SEND_MESSAGES)), 1));
            this.roleRepository.save(everyoneRole);

        }

        if (this.userRepository.count() == 0) {

            Optional<RoleDto> optional = this.roleRepository.findByName("everyone");
            UserDto admin = new UserDto();

            optional.ifPresent(roleDto -> admin.setRoles(List.of(roleDto)));

            admin.setUsername("admin");
            admin.setEnabled(true);
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setGrantedPermissions(List.of(new AssignedPermission(Permission.ADMIN)));

            this.userRepository.save(admin);

        }

    }
}
