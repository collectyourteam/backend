package de.dasshorty.collectyourteam.backend.user;

import de.dasshorty.collectyourteam.backend.user.body.AuthRequest;
import de.dasshorty.collectyourteam.backend.util.JwtUtil;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController()
@RequestMapping("/v1/validation")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final JavaMailSender mailSender;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(JavaMailSender mailSender, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.mailSender = mailSender;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {

        try {

            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username());
            final String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(token);

        } catch (AccessDeniedException exception) {

            return ResponseEntity.badRequest().body("User has to be verified!");

        } catch (Exception exception) {
            log.error("Access denied", exception);
            return ResponseEntity.status(401).body(exception);
        }

    }

    @GetMapping("/registration/{authId}")
    public ResponseEntity<?> validateRegister(@PathVariable ObjectId authId) {

        Optional<UserDto> optional = this.userRepository.findById(authId);

        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDto userDto = optional.get();

        if (!userDto.isEnabled()) {

            userDto.setEnabled(true);
            this.userRepository.save(userDto);
            return ResponseEntity.ok("User validated");

        } else {
            return ResponseEntity.badRequest().body("User already validated");
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {

        if (userDto.getEmail() == null || userDto.getUsername() == null || userDto.getPassword() == null) {
            return ResponseEntity.badRequest().body("Missing required fields");
        }

        if (!Pattern.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Invalid email address");
        }

        userDto.setEnabled(false);
        userDto.setGrantedPermissions(new ArrayList<>());
        userDto.setDeniedPermissions(new ArrayList<>());
        userDto.setCredentialsNonExpired(true);
        userDto.setAccountNonExpired(true);
        userDto.setAccountNonLocked(true);
        userDto.setRoles(new ArrayList<>());

        userDto.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        SimpleMailMessage simpleMessage = new SimpleMailMessage();

        UserDto save = this.userRepository.save(userDto);

        simpleMessage.setTo(userDto.getEmail());
        simpleMessage.setSubject("Registration on " + System.getenv("ORG_NAME"));
        simpleMessage.setFrom("no-reply@" + System.getenv("ORG_NAME"));
        simpleMessage.setText("Please click on the following link to validate your registration: http://" + System.getenv("ORG_DOMAIN") + "/v1/validation/registration/" + save.getId());

        this.mailSender.send(simpleMessage);

        return ResponseEntity.ok(save);

    }


}
