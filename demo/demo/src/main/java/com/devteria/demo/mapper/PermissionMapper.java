package com.devteria.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.devteria.demo.dto.request.PermissionRequest;
import com.devteria.demo.dto.response.PermissionResponse;
import com.devteria.demo.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    //    @Mapping(target = "permissions",ignore = true)
    Permission toEntity(PermissionRequest permissionRequest);

    void updatePermission(@MappingTarget Permission permission, PermissionRequest permissionRequest);

    //    @Mapping(source = "firstName",target = "lastName")

    PermissionResponse toResponse(Permission permission);
}
