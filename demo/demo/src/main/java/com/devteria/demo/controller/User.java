package com.devteria.demo.controller;

import com.devteria.demo.dto.response.ApiResponse;
import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.entity.UserEntity;
import com.devteria.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class User {
    @Autowired
    private UserService userService;
    @PostMapping("/users")
    ApiResponse<UserEntity> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
       return ApiResponse.<UserEntity>builder()
               .result(userService.createUser(userCreateRequest))
               .build();
    }
    @GetMapping("/users")
    ApiResponse<List<UserResponse>> getUser() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUser())
                .build();
    }
    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId,@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, userUpdateRequest))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder()
                .result("User deleted")
                .build();
    }


}
