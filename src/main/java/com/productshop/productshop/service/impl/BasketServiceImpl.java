package com.productshop.productshop.service.impl;

import com.productshop.productshop.dto.BasketDto;
import com.productshop.productshop.entity.Basket;
import com.productshop.productshop.entity.Product;
import com.productshop.productshop.entity.User;
import com.productshop.productshop.repository.BasketRepository;
import com.productshop.productshop.repository.ProductRepository;
import com.productshop.productshop.repository.UserRepository;
import com.productshop.productshop.security.jwt.JwtUser;
import com.productshop.productshop.service.BasketService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final UserRepository userDao;

    public BasketServiceImpl(BasketRepository basketRepository, ProductRepository productRepository, UserRepository userDao) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.userDao = userDao;
    }

    @Override
    public BasketDto addProduct(JwtUser jwtUser, Long productId) {
        User user = userDao.findById(jwtUser.getId()).orElseThrow(NoSuchElementException::new);
        Basket userBasket = basketRepository
                .findFirstByUserIdAndDeletedFalseOrderByUserIdDesc(jwtUser.getId());
        Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
        if (userBasket == null) {
            Basket basket = new Basket();
            ArrayList<Product> products = new ArrayList<>();
            products.add(product);
            basket.setProductList(products);
            basket.setUser(user);
            Basket savedBasket = basketRepository.save(basket);
            user.getBaskets().add(savedBasket);
            userDao.save(user);
            return savedBasket.toDto();
        } else {
            userBasket.getProductList().add(product);
            Basket savedBasket = basketRepository.save(userBasket);
            return savedBasket.toDto();
        }
    }

    @Override
    public BasketDto getLastActiveUserBasket(Long userId) {
        User user = userDao.findById(userId).orElseThrow(NoSuchElementException::new);
        Basket basket = basketRepository.findFirstByUserIdAndDeletedFalseOrderByUserIdDesc(userId);
        if (basket == null) {
            Basket newBasket = new Basket();
            ArrayList<Product> products = new ArrayList<>();
            newBasket.setProductList(products);
            newBasket.setUser(user);
            return basketRepository.save(newBasket).toDto();
        }
        return basket.toDto();
    }
}
