// package com.devteria.demo.Controller;
//
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.ArgumentMatchers;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
// import com.devteria.demo.dto.request.RoleRequest;
// import com.devteria.demo.dto.response.PermissionResponse;
// import com.devteria.demo.dto.response.RoleResponse;
// import com.devteria.demo.entity.Permission;
// import com.devteria.demo.service.RoleService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
// @SpringBootTest
// @AutoConfigureMockMvc
// @TestPropertySource("/test.properties")
// class RoleControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RoleService roleService;
//
//    private List<RoleResponse> roleResponseList = new ArrayList<>();
//    private RoleResponse roleResponse;
//    private Permission permission;
//    private PermissionResponse permissionResponse;
//    private Set<PermissionResponse> permissionResponseSet = new HashSet<>();
//    private Set<Permission> permissionSet = new HashSet<>();
//    private RoleRequest roleRequest;
//    private Set<String> permissionsCreateSet = new HashSet<>();
//
//    @BeforeEach
//    void initData() {
//        permission = Permission.builder()
//                .description("CREATE_POST")
//                .name("CREATE_POST")
//                .build();
//        permissionSet.add(permission);
//        permissionResponse = PermissionResponse.builder()
//                .description("CREATE_POST")
//                .name("CREATE_POST")
//                .build();
//        permissionResponseSet.add(permissionResponse);
//        roleResponse = RoleResponse.builder()
//                .name("USER")
//                .description("USER")
//                .permissions(permissionResponseSet)
//                .build();
//        permissionsCreateSet.add("CREATE_POST");
//        roleRequest = RoleRequest.builder()
//                .description("USER")
//                .permissions(permissionsCreateSet)
//                .build();
//        roleResponseList.add(roleResponse);
//    }
//
//    @Test
//    @WithMockUser(
//            username = "test01010101",
//            roles = {"USER"})
//    void getRoles_successful() throws Exception {
//        // GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String request = objectMapper.writeValueAsString(roleRequest);
//
//        Mockito.when(roleService.getAll()).thenReturn(roleResponseList);
//
//        // WHEN THEN
//        mockMvc.perform(MockMvcRequestBuilders.get("/roles")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(request))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    @WithMockUser(
//            username = "test01010101",
//            roles = {"USER"})
//    void deleteRole_successful() throws Exception {
//        // GIVEN
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        String request = objectMapper.writeValueAsString(roleRequest);
//
//        Mockito.doNothing().when(roleService).deleteRole(ArgumentMatchers.any());
//
//        // WHEN THEN
//        mockMvc.perform(MockMvcRequestBuilders.delete("/roles/CREATE_ROLE")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(request))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
// }
