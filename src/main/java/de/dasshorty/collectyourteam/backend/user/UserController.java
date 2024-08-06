package de.dasshorty.collectyourteam.backend.user;

import de.dasshorty.collectyourteam.backend.image.ImageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/update-image")
    public ResponseEntity<?> updateProfileImage(@RequestBody ImageDto imageDto, @AuthenticationPrincipal UserDto userDto) {
        userDto.setProfileImage(imageDto);
        return ResponseEntity.ok(userRepository.save(userDto));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/check")
    public ResponseEntity<?> checkAuthority() {
        return ResponseEntity.ok("");
    }

    @PostMapping("/add-user")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userRepository.save(userDto));
    }
}
