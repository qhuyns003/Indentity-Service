package com.devteria.demo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.entity.Role;
import com.devteria.demo.entity.UserEntity;
import com.devteria.demo.exception.AppException;
import com.devteria.demo.exception.ErrorCode;
import com.devteria.demo.mapper.RoleMapper;
import com.devteria.demo.mapper.UserMapper;
import com.devteria.demo.repository.RoleRepository;
import com.devteria.demo.repository.UserRepositoryInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepositoryInterface userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        UserEntity userEntity = userMapper.toUser(userCreateRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userEntity.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findById("USER").get());
                userEntity.setRoles(roles);
        try{
            userEntity=userRepository.save(userEntity);
        }
        catch(DataIntegrityViolationException e){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return userMapper.toUserResponse(userEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUser() {
        log.info("pass");
        List<UserEntity> userEntity = userRepository.findAll();
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserEntity userEntity1 : userEntity) {
            userResponses.add(userMapper.toUserResponse(userEntity1));
        }
        return userResponses;
    }

    @PostAuthorize("returnObject.username = authentication.name")
    public UserResponse getUser(String id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(userEntity);
    }

    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity =
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        userMapper.updateUser(userEntity, userUpdateRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        userEntity.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        //        userEntity.setRoles(new HashSet<>(userUpdateRequest.getRoles().stream().map(role
        // ->roleRepository.findById(role).orElseThrow(()->new AppException(ErrorCode.PASSWORD_INVALID))).toList()));
        List<Role> l = roleRepository.findAllById(userUpdateRequest.getRoles());
        HashSet<Role> rr = new HashSet<>(l);
        userEntity.setRoles(rr);
        UserResponse userResponse = userMapper.toUserResponse(userRepository.save(userEntity));
        userResponse.setRoles(new HashSet<>(
                userEntity.getRoles().stream().map(roleMapper::toResponse).toList()));

        return userResponse;
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserResponse getMyInfo() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity =
                userRepository.findByUsername(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(userEntity);
    }
}
