package com.productshop.productshop.dto;

import com.productshop.productshop.entity.Basket;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasketDto {

    private Long id;
    private UserDto userDto;
    private List<ProductDto> productList;
    private boolean deleted;

    public BasketDto() {
    }

    public BasketDto(Long id, UserDto userDto, List<ProductDto> productList) {
        this.id = id;
        this.userDto = userDto;
        this.productList = productList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public List<ProductDto> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDto> productList) {
        this.productList = productList;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Basket toBasket() {
        Basket basket = new Basket();
        basket.setId(this.id);
        basket.setDeleted(this.deleted);
        basket.setProductList(new ArrayList<>(
                this.productList.stream()
                        .map(ProductDto::toProduct).collect(Collectors.toList())
        ));
        basket.setUser(this.userDto.toUser());
        return basket;
    }
}
