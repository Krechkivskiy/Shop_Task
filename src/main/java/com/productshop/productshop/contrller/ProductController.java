package com.productshop.productshop.contrller;

import com.productshop.productshop.dto.ProductDto;
import com.productshop.productshop.dto.UserDto;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.service.ProductService;
import com.productshop.productshop.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping(value = "/products/upload", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto uploadProduct(@RequestBody ProductDto productDto, @RequestParam(name = "image",
            required = false) MultipartFile productPicture,
                                    @AuthenticationPrincipal JwtUser user) {
        return userService.addProduct(productDto, user, productPicture);
    }

    @PutMapping(value = "/products/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto updateProduct(@AuthenticationPrincipal JwtUser jwtUser,
                                    @RequestBody ProductDto productDto, @RequestParam(name = "image",
            required = false) MultipartFile productPicture) {
        return productService.updateProductInfo(productDto, jwtUser, productPicture);
    }

    @DeleteMapping(value = "/products/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto removeProduct(@AuthenticationPrincipal JwtUser jwtUser, @RequestBody ProductDto productDto) {
        return productService.deleteProduct(jwtUser, productDto.getId());
    }

    @GetMapping(value = "/products/get", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto getProduct(@RequestBody ProductDto productDto) {
        return productService.getById(productDto.getId());
    }
}
