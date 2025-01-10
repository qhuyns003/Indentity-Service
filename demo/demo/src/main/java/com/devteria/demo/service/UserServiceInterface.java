package com.devteria.demo.service;

import com.devteria.demo.dto.request.AuthenticationRequest;
import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UserServiceInterface {
    UserEntity createUser(UserCreateRequest userCreateRequest);
    List<UserResponse> getUser();
    UserResponse getUser(String id);
    UserResponse updateUser(String  id ,UserUpdateRequest userUpdateRequest);
    void deleteUser(String id);
}
