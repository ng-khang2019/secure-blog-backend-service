package com.project.prjauth.controller;

import com.project.prjauth.dto.request.RoleRequest;
import com.project.prjauth.dto.response.ApiResponse;
import com.project.prjauth.dto.response.RoleResponse;
import com.project.prjauth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        List<RoleResponse> roleList = roleService.getAllRoles();
        return ApiResponse.<List<RoleResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(roleList)
                .message("Retrieved all roles")
                .build();
    }

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        RoleResponse newRole = roleService.createRole(request);
        return ApiResponse.<RoleResponse>builder()
                .status(HttpStatus.CREATED.value())
                .data(newRole)
                .message("Role created successfully")
                .build();
    }

    @DeleteMapping("/{roleName}")
    public ApiResponse<Void> deleteRole(@PathVariable String roleName) {
        roleService.deleteRoleByName(roleName);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Deleted role successfully!")
                .build();
    }
}
