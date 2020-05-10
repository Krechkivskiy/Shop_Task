package com.productshop.productshop.security;

import com.productshop.productshop.entity.User;
import com.productshop.productshop.repository.UserRepository;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.security.jwt.JwtUserFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + email + " not found"));
        if (!user.getBanned()) {
            JwtUser jwtUser = JwtUserFactory.create(user);
            return jwtUser;
        } else throw new AccessDeniedException("Your account has been banned by administrator");
    }
}
