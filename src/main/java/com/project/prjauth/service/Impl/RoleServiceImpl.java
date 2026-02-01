package com.project.prjauth.service.Impl;

import com.project.prjauth.dto.request.RoleRequest;
import com.project.prjauth.dto.response.RoleResponse;
import com.project.prjauth.entity.Permission;
import com.project.prjauth.entity.Role;
import com.project.prjauth.mapper.RoleMapper;
import com.project.prjauth.repository.PermissionRepository;
import com.project.prjauth.repository.RoleRepository;
import com.project.prjauth.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);
        List<Permission> foundPermissions = permissionRepository.findByNameIn(request.getPermissions());
        role.setPermissions(new HashSet<>(foundPermissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAllWithPermissions()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteRoleByName(String name) {
        roleRepository.deleteRoleByName(name);
    }
}
