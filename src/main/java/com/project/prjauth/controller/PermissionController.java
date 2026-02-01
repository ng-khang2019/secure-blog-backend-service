package com.project.prjauth.controller;

import com.project.prjauth.dto.request.PermissionRequest;
import com.project.prjauth.dto.response.ApiResponse;
import com.project.prjauth.dto.response.PermissionResponse;
import com.project.prjauth.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {
    private final PermissionService permissionService;


    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        List<PermissionResponse> permissionList = permissionService.getAllPermissions();
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionList)
                .status(HttpStatus.OK.value())
                .message("Fetched all permissions")
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        PermissionResponse permissionResponse = permissionService.createPermission(request);
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionResponse)
                .message("Permission created successfully")
                .status(HttpStatus.CREATED.value())
                .build();
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Void> deletePermission(@PathVariable String permission) {
        permissionService.deletePermissionByName(permission);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Deleted permission successfully")
                .build();
    }
}
