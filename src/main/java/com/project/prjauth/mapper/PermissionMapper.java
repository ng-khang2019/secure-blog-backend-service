package com.project.prjauth.mapper;

import com.project.prjauth.dto.request.PermissionRequest;
import com.project.prjauth.dto.response.PermissionResponse;
import com.project.prjauth.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
