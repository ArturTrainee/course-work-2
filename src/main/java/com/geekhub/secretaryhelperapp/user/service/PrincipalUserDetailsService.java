package com.geekhub.secretaryhelperapp.user.service;

import com.geekhub.secretaryhelperapp.user.model.UserPrincipal;
import com.geekhub.secretaryhelperapp.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(PrincipalUserDetailsService.class);

    private final UserRepository userRepository;

    @Autowired
    public PrincipalUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var user = userRepository.getUserByUsername(username);
        if (user.isEmpty()) {
            logger.error("Could not find user '{}'", username);
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user.get());
    }

}
