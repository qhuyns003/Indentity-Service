package com.devteria.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.devteria.demo.dto.request.RoleRequest;
import com.devteria.demo.dto.request.RoleUpdateRequest;
import com.devteria.demo.dto.response.RoleResponse;
import com.devteria.demo.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleRequest roleRequest);
    //    @Mapping(source = "firstName",target = "lastName")
    //    @Mapping(target = "permissions",ignore = true)
    RoleResponse toResponse(Role role);

    @Mapping(target = "permissions", ignore = true)
    void updateRole(@MappingTarget Role role, RoleUpdateRequest roleUpdateRequest);
}
