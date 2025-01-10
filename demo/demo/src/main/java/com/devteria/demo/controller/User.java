package com.devteria.demo.controller;

import com.devteria.demo.dto.response.ApiResponse;
import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.entity.UserEntity;
import com.devteria.demo.enums.Role;
import com.devteria.demo.service.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class User {
    @Autowired
    private UserServiceInterface userService;
    @PostMapping("/users")
    ResponseEntity<ApiResponse<UserEntity>> createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        ApiResponse<UserEntity> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.createUser(userCreateRequest));
       return ResponseEntity.ok().body(apiResponse);
    }
    @GetMapping("/users")
    ResponseEntity<ApiResponse<List<UserResponse>>> getUser() {

        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUser());
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/{userId}")
    ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable String userId) {
        ApiResponse apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.getUser(userId));

        return ResponseEntity.ok().body(apiResponse);
    }

    @PutMapping("/{userId}")
    ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.updateUser(userId, userUpdateRequest));

        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User deleted";
    }

    @GetMapping("/test")
    String hello(){
        return "Hello World";
    }
}
