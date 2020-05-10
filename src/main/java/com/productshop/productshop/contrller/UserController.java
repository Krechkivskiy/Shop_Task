package com.productshop.productshop.contrller;

import com.productshop.productshop.dto.UserDto;
import com.productshop.productshop.entity.User;
import com.productshop.productshop.security.jwt.JwtTokenProvider;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.service.MailService;
import com.productshop.productshop.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
public class UserController {

    @Value("server.url")
    private String url;

    private final UserService userService;
    private final MailService mailService;
    private final JwtTokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, MailService mailService, JwtTokenProvider tokenProvider,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.mailService = mailService;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration")
    public UserDto registration(UserDto userDto) {
        return userService.save(userDto, "ROLE_USER");
    }

    @PostMapping("/restore")
    public boolean passwordRestore(String email) {
        String token = tokenProvider.createToken(email, new ArrayList<>());
        String message = url + "/restore/" + token;
        mailService.send(message, token);
        return true;
    }

    @PutMapping("/restore/{token}")
    public String passwordRestore(@PathVariable(name = "token") String authToken, String newPassword) {
        if (tokenProvider.validateToken(authToken)) {
            User user = userService.findByEmail(tokenProvider.getUsername(authToken))
                    .orElseThrow(NoSuchElementException::new);
            user.setPassword(passwordEncoder.encode(newPassword));
            return "success";
        }
        return "error";
    }

    @PostMapping("/users/delete")
    public void deleteUser(UserDto userDto, @AuthenticationPrincipal JwtUser user) {
        if (user.getId().equals(userDto.getId())) {
            userService.deleteUser(userDto.getId());
        } else throw new AccessDeniedException("Error. You can delete only own account");
    }

    @PostMapping("/users/update")
    public UserDto updateUser(@AuthenticationPrincipal JwtUser user, UserDto userDto) {
        if (userDto.getId().equals(user.getId())) {
            return userService.update(user, userDto);
        } else throw new AccessDeniedException("Error, you can update only own profile");

    }

    @PutMapping("/admin/ban")
    public UserDto banUser(@AuthenticationPrincipal JwtUser user, Long userId) {
        if (user.getAuthorities().stream().anyMatch(r -> r.equals("ROLE_ADMIN"))) {
            return userService.ban(userId);
        } else throw new AccessDeniedException("Only admin can ban users");
    }
}
