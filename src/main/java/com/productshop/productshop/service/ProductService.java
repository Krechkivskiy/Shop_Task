package com.productshop.productshop.service;


import com.productshop.productshop.dto.ProductDto;
import com.productshop.productshop.dto.UserDto;
import com.productshop.productshop.security.jwt.JwtUser;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductDto updateProductInfo(ProductDto productDto, JwtUser user, MultipartFile file);

    ProductDto getById(Long id);

    UserDto deleteProduct(JwtUser user, Long productId);
}
