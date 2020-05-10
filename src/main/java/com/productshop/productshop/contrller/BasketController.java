package com.productshop.productshop.contrller;

import com.itextpdf.text.DocumentException;
import com.productshop.productshop.dto.BasketDto;
import com.productshop.productshop.dto.ProductDto;
import com.productshop.productshop.entity.Picture;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.service.BasketService;
import com.productshop.productshop.util.CheckWriter;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping("/baskets/add/product")
    public BasketDto addProductToBasket(@AuthenticationPrincipal JwtUser jwtUser, ProductDto productDto) {
        return basketService.addProduct(jwtUser, productDto.getId());
    }

    @GetMapping("/baskets/get")
    public BasketDto getBasketByUser(@AuthenticationPrincipal JwtUser jwtUser) {
        return basketService.getLastActiveUserBasket(jwtUser.getId());
    }

    @GetMapping("/baskets/check")
    public ResponseEntity<Resource> generateCheck(@AuthenticationPrincipal JwtUser jwtUser)
            throws IOException, DocumentException {
        BasketDto basket = basketService.getLastActiveUserBasket(jwtUser.getId());
        List<ProductDto> productList = basket.getProductList();
        Picture picture = new Picture();
        picture.setName(basket.getId().toString());
        File dir = new File("src/main/resources/checks/" + jwtUser.getId() + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File check = new File(dir.getAbsolutePath() + "/" + basket.getId() + ".pdf");
        check.createNewFile();
        CheckWriter.writeCheck(productList, basket.getId(), check.getAbsolutePath());
        return null;
    }
}