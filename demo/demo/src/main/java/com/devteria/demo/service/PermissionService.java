package com.devteria.demo.service;

import com.devteria.demo.dto.request.PermissionRequest;
import com.devteria.demo.dto.response.PermissionResponse;
import com.devteria.demo.mapper.PermissionMapper;
import com.devteria.demo.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionService {
    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;
    public List<PermissionResponse> getAll(){
        return permissionRepository.findAll().stream().map(permissionMapper::toResponse).toList();
    }

    public void deletePermission(String id){
        permissionRepository.deleteById(id);
    }

    public PermissionResponse createPermission(PermissionRequest permissionRequest){
        return permissionMapper.toResponse(permissionRepository.save(permissionMapper.toEntity(permissionRequest)));
    }


}
