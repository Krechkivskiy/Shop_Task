package com.productshop.productshop.contrller;

import com.productshop.productshop.dto.AuthenticationRequestDto;
import com.productshop.productshop.dto.RoleDto;
import com.productshop.productshop.dto.UserDto;
import com.productshop.productshop.entity.Role;
import com.productshop.productshop.entity.User;
import com.productshop.productshop.security.jwt.JwtTokenProvider;
import com.productshop.productshop.service.RoleService;
import com.productshop.productshop.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
public class InitController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;

    public InitController(UserService userService, JwtTokenProvider tokenProvider, BCryptPasswordEncoder encoder,
                          AuthenticationManager authenticationManager, RoleService roleService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String email = requestDto.getLogin();
            User user = userService.findByEmail(email).orElseThrow(NoSuchElementException::new);
            if (!user.getBanned()) {
                user.setRoles(new ArrayList<>());
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,
                        requestDto.getPassword()));
                String token = tokenProvider.createToken(email, user.getRoles());
                return ResponseEntity.ok("Bearer " + token);
            } else throw new AccessDeniedException("You have been banned, access denied ");
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostConstruct()
    public void getAllEmployees() {
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");
        roleService.addRole(roleUser.toDto());
        RoleDto roleAdmin = new RoleDto();
        roleAdmin.setName("ROLE_ADMIN");
        roleService.addRole(roleAdmin);
        User user = new User();
        user.setEmail("vasya");
        user.setPassword(encoder.encode("pupkin"));
        user.setFirstName("asd");
        userService.save(user.toDto(), "ROLE_ADMIN");
    }
}
