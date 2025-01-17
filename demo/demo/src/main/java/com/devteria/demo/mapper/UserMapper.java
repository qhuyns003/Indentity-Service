package com.devteria.demo.mapper;

import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

//    @Mapping(target = "lastName",ignore = true)
    UserEntity toUser(UserCreateRequest userCreateRequest);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget UserEntity userEntity , UserUpdateRequest userUpdateRequest);

//    @Mapping(source = "firstName",target = "lastName")
    @Mapping(target = "roles", ignore = true)
    UserResponse toUserResponse(UserEntity userEntity);
}
