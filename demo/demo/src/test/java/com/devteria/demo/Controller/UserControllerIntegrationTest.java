package com.devteria.demo.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.repository.UserRepositoryInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private MockMvc mockMvc;

    private UserCreateRequest userCreateRequest;
    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;
    private List<UserResponse> userResponseList;

    @Autowired
    private UserRepositoryInterface userRepositoryInterface;

    @BeforeEach
    void initData() {

        HashSet<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        LocalDate dob = LocalDate.of(2000, 2, 2);
        userCreateRequest = UserCreateRequest.builder()
                .username("admin1")
                .password("123456")
                .firstName("Ad")
                .lastName("min")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("test01010101")
                .username("admin1")
                .firstName("Ad")
                .lastName("min")
                .dob(dob)
                .build();

        userUpdateRequest = UserUpdateRequest.builder()
                .dob(dob)
                .firstName("Ad")
                .lastName("min")
                .password("12345")
                .roles(roles)
                .build();

        userResponseList = new ArrayList<>();
        userResponseList.add(userResponse);
    }

    @Test
    void createUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createUser_username_failed() throws Exception {
        // GIVEN
        userCreateRequest.setUsername("adm");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003));
    }

    @Test
    void createUser_password_failed() throws Exception {
        // GIVEN
        userCreateRequest.setPassword("1");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1004));
    }

    @Test
    void createUser_dob_failed() throws Exception {
        LocalDate dob = LocalDate.now();
        // GIVEN
        userCreateRequest.setDob(dob);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1010));
    }

    @Test
    @WithMockUser(
            username = "testuser",
            roles = {"USER"})
    void deleteUser_validRequest_success() throws Exception {
        // WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders.delete("/123"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(
            username = "testuser",
            roles = {"USER"})
    void updateUser_validRequest_success() throws Exception {
        // GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userUpdateRequest);
        String adminId = userRepositoryInterface.findByUsername("admin").get().getId();
        // WHEN THEN

        mockMvc.perform(MockMvcRequestBuilders.put("/" + adminId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
        //                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("test01010101"))
        //                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("Ad"))
        ;
    }
}
