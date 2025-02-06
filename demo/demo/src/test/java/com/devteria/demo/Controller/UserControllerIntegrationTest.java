package com.devteria.demo.Controller;

import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.entity.UserEntity;
import com.devteria.demo.repository.UserRepositoryInterface;
import com.devteria.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driverClassName", ()->"com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", ()->"update");
    }
    @Autowired
    private MockMvc mockMvc;

    private UserCreateRequest userCreateRequest;
    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;
    private List<UserResponse> userResponseList;
    private String userId;

    @Autowired
    private UserRepositoryInterface userRepositoryInterface;



    @BeforeEach
    void initData(){

        UserEntity userEntity = UserEntity.builder()
                .username("12345")
                .build();
        userRepositoryInterface.save(userEntity);
        HashSet<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        LocalDate dob= LocalDate.of(2000,2,2);

        userCreateRequest = UserCreateRequest.builder()
                .username("admin1")
                .password("123456")
                .firstName("Ad")
                .lastName("min")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("test01010101")
                .username("admin")
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

        userId = "test01010101";

    }

    @Test
    void createUser_validRequest_success() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void createUser_username_failed() throws Exception {
        //GIVEN
        userCreateRequest.setUsername("adm");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1003));

    }

    @Test
    void createUser_password_failed() throws Exception {
        //GIVEN
        userCreateRequest.setPassword("1");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1004));

    }

    @Test
    void createUser_dob_failed() throws Exception {
        LocalDate dob= LocalDate.now();
        //GIVEN
        userCreateRequest.setDob(dob);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("code").value(1010));

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void deleteUser_validRequest_success() throws Exception {
        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/123"))
                        .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void updateUser_validRequest_success() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userUpdateRequest);
//
//        Mockito.when(userService.updateUser(ArgumentMatchers.any(),ArgumentMatchers.any()))
//                .thenReturn(userResponse);
        //WHEN THEN
        String userId = userRepositoryInterface.findByUsername("12345").get().getId();
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/"+userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("test01010101"))
//                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("Ad"))
        ;

    }
//
//    @Test
//    @WithMockUser(username = "test01010101", roles = {"USER"})
//    void getUser_successful() throws Exception {
//        //GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String request = objectMapper.writeValueAsString(userCreateRequest);
//
//        Mockito.when(userService.getUser()).thenReturn(userResponseList);
//        //WHEN THEN
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/users")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(request))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//    }
//
//    @Test
//    @WithMockUser(username = "test01010101", roles = {"USER"})
//    void getUserById_successful() throws Exception {
//        //GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String request = objectMapper.writeValueAsString(userCreateRequest);
//
//        Mockito.when(userService.getUser(ArgumentMatchers.any())).thenReturn(userResponse);
//
//        //WHEN THEN
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/test01010101")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(request))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//    }
//
//    @Test
//    @WithMockUser(username = "test01010101", roles = {"USER"})
//    void getMyInfo_successful() throws Exception {
//        //GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String request = objectMapper.writeValueAsString(userCreateRequest);
//
//        Mockito.when(userService.getMyInfo()).thenReturn(userResponse);
//
//        //WHEN THEN
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/myInfo")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(request))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("test01010101"));
//
//
//    }







}
