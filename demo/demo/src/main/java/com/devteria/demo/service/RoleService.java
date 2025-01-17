package com.devteria.demo.service;

import com.devteria.demo.dto.request.RoleRequest;
import com.devteria.demo.dto.request.RoleUpdateRequest;
import com.devteria.demo.dto.response.RoleResponse;
import com.devteria.demo.entity.Role;
import com.devteria.demo.exception.AppException;
import com.devteria.demo.exception.ErrorCode;
import com.devteria.demo.mapper.PermissionMapper;
import com.devteria.demo.mapper.RoleMapper;
import com.devteria.demo.repository.PermissionRepository;
import com.devteria.demo.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleService {
    final RoleRepository roleRepository;
    final RoleMapper roleMapper;
    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;

    public List<RoleResponse> getAll(){
        return roleRepository.findAll().stream().map(roleMapper::toResponse).toList();
    }

     public void deleteRole(String id) {
        roleRepository.deleteById(id);
     }

     public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.existsById(roleRequest.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }
         Role role = roleMapper.toEntity(roleRequest);
         role.setPermissions(new HashSet<>(permissionRepository.findAllById(roleRequest.getPermissions())));
        return roleMapper.toResponse(roleRepository.save(role));
     }

     public RoleResponse updateRole(String id, RoleUpdateRequest roleUpdateRequest) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roleMapper.updateRole(role, roleUpdateRequest);
        role.setPermissions(new HashSet<>(permissionRepository.findAllById(roleUpdateRequest.getPermissions())));
        RoleResponse roleResponse = roleMapper.toResponse(roleRepository.save(role));
        roleResponse.setPermissions(new HashSet<>(role.getPermissions().stream().map(permissionMapper::toResponse).toList()));
        return roleResponse;

     }

}
