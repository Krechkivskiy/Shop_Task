package com.productshop.productshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.productshop.productshop.entity.Role;
import com.productshop.productshop.entity.User;

import javax.persistence.*;
import java.util.List;

public class RoleDto {

    private Long id;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Role toRole() {
        Role role = new Role();
        role.setName(this.name);
        role.setId(this.id);
        return role;
    }
}
