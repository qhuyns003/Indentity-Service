package com.devteria.demo.service.impl;

import com.devteria.demo.dto.request.AuthenticationRequest;
import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.entity.UserEntity;
import com.devteria.demo.enums.Role;
import com.devteria.demo.exception.ErrorCode;
import com.devteria.demo.exception.AppException;
import com.devteria.demo.mapper.UserMapper;
import com.devteria.demo.repository.UserRepositoryInterface;
import com.devteria.demo.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserServiceInterface {
    @Autowired
    private UserRepositoryInterface userRepository;
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserEntity createUser(UserCreateRequest userCreateRequest) {
        UserEntity userEntity = userMapper.toUser(userCreateRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userEntity.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        if(userRepository.existsByUsername(userCreateRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Set<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        userEntity.setRoles(roles);

        return userRepository.save(userEntity);
    }

    @Override
    public List<UserResponse> getUser() {
        List<UserEntity> userEntity = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for(UserEntity userEntity1 : userEntity) {
            userResponses.add(userMapper.toUserResponse(userEntity1));
        }
        return  userResponses;
    }

    @Override
    public UserResponse getUser(String id) {
        UserEntity userEntity= userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UserResponse userResponse = userMapper.toUserResponse(userEntity);

        return userResponse;
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity=userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(userEntity, userUpdateRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userEntity.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        return userMapper.toUserResponse(userRepository.save(userEntity));
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }



}
