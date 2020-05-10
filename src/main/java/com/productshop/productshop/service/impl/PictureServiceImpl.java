package com.productshop.productshop.service.impl;

import com.productshop.productshop.entity.Picture;
import com.productshop.productshop.repository.PictureRepository;
import com.productshop.productshop.repository.ProductRepository;
import com.productshop.productshop.repository.UserRepository;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.service.PictureService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PictureServiceImpl implements PictureService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;

    public PictureServiceImpl(ProductRepository productRepository, UserRepository userRepository,
                              PictureRepository pictureRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public List<Picture> getPicturesByProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(NoSuchElementException::new).getPictures();
    }

    @Override
    public boolean removePictureByName(Long pictureId, Long productId, JwtUser jwtUser) {
        try {
            Picture picture = userRepository.findById(jwtUser.getId())
                    .orElseThrow(NoSuchElementException::new)
                    .getProducts()
                    .stream().filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElseThrow(() -> new AccessDeniedException("You can remove only own projects"))
                    .getPictures()
                    .stream()
                    .filter(p -> p.getId().equals(pictureId))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Error, Picture not found"));
            pictureRepository.delete(picture);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
