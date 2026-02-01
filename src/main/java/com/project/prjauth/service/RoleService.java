package com.project.prjauth.service;

import com.project.prjauth.dto.request.RoleRequest;
import com.project.prjauth.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleRequest request);
    List<RoleResponse> getAllRoles();
    void deleteRoleByName(String name);

}
