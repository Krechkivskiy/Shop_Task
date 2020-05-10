package com.productshop.productshop.service;

import com.productshop.productshop.dto.RoleDto;
import com.productshop.productshop.entity.Role;

public interface RoleService {

    Role addRole(RoleDto roleDto);

    RoleDto getById(Long roleId);
}
