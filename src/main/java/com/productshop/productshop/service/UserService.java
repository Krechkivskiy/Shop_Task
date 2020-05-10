package com.productshop.productshop.service;

import com.productshop.productshop.dto.ProductDto;
import com.productshop.productshop.dto.UserDto;
import com.productshop.productshop.entity.User;
import com.productshop.productshop.security.jwt.JwtUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    UserDto save(UserDto user,String role);

    Optional<User> findByEmail(String email);

    void deleteUser(Long id);

    UserDto ban(Long userId);

    UserDto update(JwtUser user, UserDto userDto);

    ProductDto addProduct(ProductDto productDto, JwtUser user, MultipartFile productPicture);
}
