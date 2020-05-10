package com.productshop.productshop.dto;

import com.productshop.productshop.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<BasketDto> basket;
    private List<ProductDto> ownProducts;
    private List<RoleDto> roleDtos;

    public UserDto() {
    }

    public User toUser() {
        User user = new User();
        user.setEmail(this.email);
        user.setFirstName(this.firstName);
        user.setId(this.id);
        user.setEmail(this.email);
        user.setLastName(this.lastName);
        user.setPassword(this.password);
        if (this.roleDtos != null) {
            user.setRoles(this.roleDtos.stream()
                    .map(RoleDto::toRole)
                    .collect(Collectors.toList()));
        }
        if (this.ownProducts != null) {
            user.setProducts(this.ownProducts.stream()
                    .map(ProductDto::toProduct)
                    .collect(Collectors.toList()));
        }
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BasketDto> getBasket() {
        return basket;
    }

    public void setBasket(List<BasketDto> basket) {
        this.basket = basket;
    }

    public List<ProductDto> getOwnProducts() {
        return ownProducts;
    }

    public void setOwnProducts(List<ProductDto> ownProducts) {
        this.ownProducts = ownProducts;
    }

    public List<RoleDto> getRoleDtos() {
        return roleDtos;
    }

    public void setRoleDtos(List<RoleDto> roleDtos) {
        this.roleDtos = roleDtos;
    }
}
