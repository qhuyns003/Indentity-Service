package com.devteria.demo.Controller;

import com.devteria.demo.dto.request.UserCreateRequest;
import com.devteria.demo.dto.request.UserUpdateRequest;
import com.devteria.demo.dto.response.ApiResponse;
import com.devteria.demo.dto.response.UserResponse;
import com.devteria.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.test.context.support.WithMockUser;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    private UserCreateRequest userCreateRequest;
    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;
    private List<UserResponse> userResponseList;
    private String userId;



    @BeforeEach
    void initData(){
        HashSet<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        LocalDate dob= LocalDate.of(2000,2,2);

        userCreateRequest = UserCreateRequest.builder()
                .username("admin")
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
        Mockito.when(userService.createUser(ArgumentMatchers.any()))
                .thenReturn(userResponse);
        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("test01010101"));

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
        //GIVEN
        Mockito.doNothing().when(userService).deleteUser(ArgumentMatchers.any());
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
        String request = objectMapper.writeValueAsString(userCreateRequest);

        Mockito.when(userService.updateUser(ArgumentMatchers.any(),ArgumentMatchers.any()))
                .thenReturn(userResponse);
        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/12345")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("test01010101"))
                .andExpect(MockMvcResultMatchers.jsonPath("result.firstName").value("Ad"))
        ;

    }

    @Test
    @WithMockUser(username = "test01010101", roles = {"USER"})
    void getUser_successful() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        Mockito.when(userService.getUser()).thenReturn(userResponseList);
        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "test01010101", roles = {"USER"})
    void getUserById_successful() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        Mockito.when(userService.getUser(ArgumentMatchers.any())).thenReturn(userResponse);

        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/test01010101")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "test01010101", roles = {"USER"})
    void getMyInfo_successful() throws Exception {
        //GIVEN
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String request = objectMapper.writeValueAsString(userCreateRequest);

        Mockito.when(userService.getMyInfo()).thenReturn(userResponse);

        //WHEN THEN
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/myInfo")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("result.id").value("test01010101"));


    }







}
