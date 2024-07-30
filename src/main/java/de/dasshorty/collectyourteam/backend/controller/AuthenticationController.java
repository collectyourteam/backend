package de.dasshorty.collectyourteam.backend.controller;

import de.dasshorty.collectyourteam.backend.controller.body.AuthRequest;
import de.dasshorty.collectyourteam.backend.user.UserDetailsService;
import de.dasshorty.collectyourteam.backend.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {

        try {

            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));

            final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.username());
            final String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(token);

        } catch (AccessDeniedException exception) {
            log.error("Access denied", exception);
            return ResponseEntity.status(401).body(exception);
        }


    }


}
