package com.productshop.productshop.service;

import com.productshop.productshop.dto.BasketDto;
import com.productshop.productshop.security.jwt.JwtUser;

public interface BasketService {

    BasketDto addProduct(JwtUser user, Long product);

    BasketDto getLastActiveUserBasket(Long userId);

}
