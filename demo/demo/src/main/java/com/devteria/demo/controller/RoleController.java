package com.devteria.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.devteria.demo.dto.request.RoleRequest;
import com.devteria.demo.dto.request.RoleUpdateRequest;
import com.devteria.demo.dto.response.ApiResponse;
import com.devteria.demo.dto.response.RoleResponse;
import com.devteria.demo.mapper.RoleMapper;
import com.devteria.demo.service.RoleService;

@RequestMapping("/roles")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @GetMapping
    ApiResponse<List<RoleResponse>> getRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return ApiResponse.<String>builder().result("succesfully deleted").build();
    }

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(roleRequest))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<RoleResponse> updateRole(@PathVariable String id, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(id, roleUpdateRequest))
                .build();
    }
}
