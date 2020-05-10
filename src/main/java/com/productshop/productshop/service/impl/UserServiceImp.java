package com.productshop.productshop.service.impl;

import com.productshop.productshop.dto.ProductDto;
import com.productshop.productshop.dto.UserDto;
import com.productshop.productshop.entity.Picture;
import com.productshop.productshop.entity.Product;
import com.productshop.productshop.entity.Role;
import com.productshop.productshop.entity.User;
import com.productshop.productshop.repository.PictureRepository;
import com.productshop.productshop.repository.ProductRepository;
import com.productshop.productshop.repository.RoleRepository;
import com.productshop.productshop.repository.UserRepository;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.service.UserService;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RoleRepository roleRepository;
    private final PictureRepository pictureRepository;

    public UserServiceImp(UserRepository userRepository, ProductRepository productRepository,
                          RoleRepository roleRepository, PictureRepository pictureRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.roleRepository = roleRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public UserDto save(UserDto userDto, String roleName) {
        Role role = roleRepository.findByName(roleName).orElseThrow(NoSuchElementException::new);
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("This email have already busied ");
        }
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);
        User saved = userRepository.save(userDto.toUser());
        saved.setRoles(roles);
        return userRepository.save(saved).toDto();
    }

    @Override
    public ProductDto addProduct(ProductDto productDto, JwtUser user, MultipartFile productImage) {
        User fromDb = userRepository.findByEmail(user.getEmail()).orElseThrow(NoSuchElementException::new);
        Product savedProduct = productRepository.save(productDto.toProduct());
        if (productImage != null) {
            try {
                Picture picture = new Picture();
                picture.setName(productImage.getOriginalFilename());
                File dir = new File("images/" + user.getId() + "/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File convertFile = new File(dir.getAbsolutePath() + "/" + productImage.getOriginalFilename());
                picture.setPath(convertFile.getAbsolutePath());
                FileOutputStream outputStream = new FileOutputStream(convertFile);
                outputStream.write(productImage.getBytes());
                outputStream.close();
                convertFile.createNewFile();
                Picture savedPicture = pictureRepository.save(picture);
                savedProduct.getPictures().add(savedPicture);
                fromDb.getProducts().add(savedProduct);
                userRepository.save(fromDb).toDto();
                return savedProduct.toDto();
            } catch (IOException e) {
            }
        }
        fromDb.getProducts().add(savedProduct);
        User usr = userRepository.save(fromDb);
        return usr.getProducts().get(usr.getProducts().size() - 1).toDto();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public UserDto ban(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        user.setBanned(true);
        return userRepository.save(user).toDto();
    }

    @Override
    public UserDto update(JwtUser jwtUser, UserDto userDto) {
        if (jwtUser.getId().equals(userDto.getId())) {
            User user = userRepository.findById(jwtUser.getId()).orElseThrow(NoSuchElementException::new);
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            return userRepository.save(user).toDto();
        } else throw new PermissionDeniedDataAccessException("You can update only own profile", new Throwable());
    }
}
