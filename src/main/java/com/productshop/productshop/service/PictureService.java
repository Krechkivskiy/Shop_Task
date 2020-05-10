package com.productshop.productshop.service;

import com.productshop.productshop.entity.Picture;
import com.productshop.productshop.security.jwt.JwtUser;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureService {

    List<Picture> getPicturesByProduct(Long productId);

    boolean removePictureByName(Long pictureId, Long productId, JwtUser jwtUser);
}
