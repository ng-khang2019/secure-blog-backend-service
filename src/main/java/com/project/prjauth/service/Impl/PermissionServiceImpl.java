package com.project.prjauth.service.Impl;

import com.project.prjauth.dto.request.PermissionRequest;
import com.project.prjauth.dto.response.PermissionResponse;
import com.project.prjauth.entity.Permission;
import com.project.prjauth.mapper.PermissionMapper;
import com.project.prjauth.repository.PermissionRepository;
import com.project.prjauth.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionServiceImpl implements PermissionService {
    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Permission already exists");
        }
        Permission permission = permissionMapper.toPermission(request);

        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    @Transactional
    public void deletePermissionByName(String name) {
        permissionRepository.deletePermissionByName(name);
    }
}
