package com.productshop.productshop.service.impl;

import com.productshop.productshop.dto.RoleDto;
import com.productshop.productshop.entity.Role;
import com.productshop.productshop.repository.RoleRepository;
import com.productshop.productshop.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl  implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role addRole(RoleDto roleDto) {
        return roleRepository.save(roleDto.toRole());
    }

    @Override
    public RoleDto getById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(NoSuchFieldError::new).toDto();
    }
}
