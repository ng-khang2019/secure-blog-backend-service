package com.project.prjauth.service;

import com.project.prjauth.dto.request.PasswordUpdateRequest;
import com.project.prjauth.dto.request.UserRegistrationRequest;
import com.project.prjauth.dto.request.UserUpdateRequest;
import com.project.prjauth.dto.response.UserDetailsResponse;
import com.project.prjauth.dto.response.UserProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserProfileResponse getCurrentUserProfile();
    UserDetailsResponse getCurrentUserDetails();
    UserProfileResponse getUserProfileById(Long id);
    UserProfileResponse getUserProfileByUsername(String username);
    UserDetailsResponse getUserDetailsById(Long id);
    Page<UserDetailsResponse> getUsers(Pageable pageable);
    UserDetailsResponse createUser(UserRegistrationRequest request);
    UserDetailsResponse updateUser(Long id, UserUpdateRequest request);
    UserDetailsResponse updateUser(UserUpdateRequest request);
    void updatePassword(PasswordUpdateRequest request);
    void deleteUserById(Long id);
}
