package com.project.prjauth.service;

import com.project.prjauth.dto.request.PermissionRequest;
import com.project.prjauth.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(PermissionRequest request);
    List<PermissionResponse> getAllPermissions();
    void deletePermissionByName(String name);
}
