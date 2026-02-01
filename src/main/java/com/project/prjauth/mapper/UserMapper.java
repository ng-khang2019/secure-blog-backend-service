package com.project.prjauth.mapper;

import com.project.prjauth.dto.request.UserRegistrationRequest;
import com.project.prjauth.dto.request.UserUpdateRequest;
import com.project.prjauth.dto.response.UserDetailsResponse;
import com.project.prjauth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    User toUser(UserRegistrationRequest request);

    @Mapping(target = "imageUrl", source = "avatarImage.url")
    UserDetailsResponse toUserDetailsResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
