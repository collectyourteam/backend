package de.dasshorty.collectyourteam.backend.util;

import de.dasshorty.collectyourteam.backend.user.UserDto;
import de.dasshorty.collectyourteam.backend.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DataGenerator implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataGenerator(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.userRepository.count() == 0) {

            UserDto admin = new UserDto();

            Optional<RoleDto> optionalRoleDto = this.roleRepository.findByName("ADMIN");

            if (optionalRoleDto.isEmpty()) {
                RoleDto role = new RoleDto();
                role.setName("ADMIN");
                role.setHexColor("#ffffff");
                this.roleRepository.save(role);
                optionalRoleDto = Optional.of(role);
            }

            admin.setRoles(List.of(optionalRoleDto.get().getId()));
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));

            this.userRepository.save(admin);

        }

    }
}
