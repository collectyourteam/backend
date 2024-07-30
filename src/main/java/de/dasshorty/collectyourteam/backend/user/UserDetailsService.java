package de.dasshorty.collectyourteam.backend.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository myUserRepo) {
        this.userRepository = myUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserDto> byUsername = userRepository.findByUsername(username);

        if (byUsername.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return byUsername.get();

    }

}
