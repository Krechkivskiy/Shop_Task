package com.productshop.productshop.service.impl;

import com.productshop.productshop.dto.ProductDto;
import com.productshop.productshop.dto.UserDto;
import com.productshop.productshop.entity.Picture;
import com.productshop.productshop.entity.Product;
import com.productshop.productshop.entity.User;
import com.productshop.productshop.repository.PictureRepository;
import com.productshop.productshop.repository.ProductRepository;
import com.productshop.productshop.repository.UserRepository;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.service.ProductService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository,
                              PictureRepository pictureRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public ProductDto updateProductInfo(ProductDto productDto, JwtUser user, MultipartFile file) {
        User fromDb = userRepository.findById(user.getId()).orElseThrow(NoSuchElementException::new);
        Product product = fromDb.getProducts().stream()
                .filter(p -> p.getId().equals(productDto.getId()))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("You can update only own products"));
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        if (file != null) {
            product.getPictures().clear();
            try {
                Picture picture = new Picture();
                picture.setName(file.getOriginalFilename());
                File dir = new File("images/" + user.getId() + "/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File convertFile = new File(dir.getAbsolutePath() + "/" + file.getOriginalFilename());
                picture.setPath(convertFile.getAbsolutePath());
                FileOutputStream outputStream = new FileOutputStream(convertFile);
                outputStream.write(file.getBytes());
                outputStream.close();
                convertFile.createNewFile();
                Picture savedPicture = pictureRepository.save(picture);
                product.getPictures().add(savedPicture);
                fromDb.getProducts().add(product);
                userRepository.save(fromDb).toDto();
                return productRepository.save(product).toDto();
            } catch (IOException e) {
            }
        }
        return productRepository.save(product).toDto();
    }

    @Override
    public ProductDto getById(Long id) {
        return productRepository.findById(id).orElseThrow(NoSuchElementException::new).toDto();
    }

    @Override
    public UserDto deleteProduct(JwtUser jwtUser, Long productId) {
        User user = userRepository.findByEmail(jwtUser.getEmail()).orElseThrow(NoSuchElementException::new);
        Product product = user.getProducts().stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst().orElseThrow(() -> new AccessDeniedException("You can remove only own products"));
        user.getProducts().remove(product);
        userRepository.save(user);
        return user.toDto();
    }
}
