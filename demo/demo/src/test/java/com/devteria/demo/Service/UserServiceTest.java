package com.devteria.demo.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.entity.Role;
import com.devteria.demo.entity.UserEntity;
import com.devteria.demo.repository.RoleRepository;
import com.devteria.demo.repository.UserRepositoryInterface;
import com.devteria.demo.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepositoryInterface userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private SecurityContext securityContext;

    @MockBean
    private Authentication authentication;

    private UserCreateRequest userCreateRequest;
    private UserEntity user;
    private List<Role> roles;
    private UserUpdateRequest userUpdateRequest;
    private Role role;

    private LocalDate dob = LocalDate.of(2000, 2, 2);

    @BeforeEach
    void initData() {
        roles = new ArrayList<>();
        HashSet<String> updateRole = new HashSet<>();
        updateRole.add("ADMIN");
        roles.add(role);

        userCreateRequest = UserCreateRequest.builder()
                .username("admin")
                .password("123456")
                .firstName("Ad")
                .lastName("min")
                .dob(dob)
                .build();

        user = UserEntity.builder()
                .id("test01010101")
                .username("admin")
                .firstName("Ad")
                .lastName("min")
                .dob(dob)
                .roles(new HashSet<>(roles))
                .build();

        userUpdateRequest = UserUpdateRequest.builder()
                .dob(dob)
                .firstName("Ad")
                .lastName("min")
                .password("12345")
                .roles(updateRole)
                .build();

        role = Role.builder().name("ADMIN").description("Admin").build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.existsByUsername(ArgumentMatchers.any())).thenReturn(false);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(role));

        // WHEN
        var response = userService.createUser(userCreateRequest);

        // THEN
        Assertions.assertThat(response.getFirstName()).isEqualTo("Ad");
    }

    @Test
    void deleteUser_validRequest_success() {
        // GIVEN
        // WHEN
        userService.deleteUser("test01010101");
        // THEN
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(ArgumentMatchers.any());
    }

    @Test
    void updateUser_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(user));
        Mockito.when(roleRepository.findAllById(ArgumentMatchers.any())).thenReturn(roles);
        Mockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(role));

        // WHEN
        var response = userService.updateUser(user.getId(), userUpdateRequest);

        // THEN
        Assertions.assertThat(response.getFirstName()).isEqualTo("Ad");
    }

    @Test
    void getMyInfo_validRequest_success() {
        // GIVEN
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(user));
        SecurityContextHolder.setContext(securityContext);
        // Đặt securityContext (mock) vào SecurityContextHolder để khi SecurityContextHolder.getContext() sẽ gọi ra mock
        // object
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("test01010101");

        // WHEN
        var response = userService.getMyInfo();

        // THEN
        Assertions.assertThat(response.getFirstName()).isEqualTo("Ad");
    }
}
