package com.productshop.productshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.productshop.productshop.entity.Basket;
import com.productshop.productshop.entity.Product;

import java.util.List;

public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private double price;

    @JsonIgnore
    private List<Basket> basketList;

    public ProductDto() {
    }

    public  Product toProduct(){
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        return product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Basket> getBasketList() {
        return basketList;
    }

    public void setBasketList(List<Basket> basketList) {
        this.basketList = basketList;
    }
}
