package com.project.prjauth.service.Impl;

import com.project.prjauth.constant.PredefinedRole;
import com.project.prjauth.dto.request.PasswordUpdateRequest;
import com.project.prjauth.dto.request.UserRegistrationRequest;
import com.project.prjauth.dto.request.UserUpdateRequest;
import com.project.prjauth.dto.response.UserDetailsResponse;
import com.project.prjauth.dto.response.UserProfileResponse;
import com.project.prjauth.entity.Role;
import com.project.prjauth.entity.User;
import com.project.prjauth.exception.ResourceNotFoundException;
import com.project.prjauth.mapper.UserMapper;
import com.project.prjauth.repository.RoleRepository;
import com.project.prjauth.repository.UserDisplayRepository;
import com.project.prjauth.repository.UserRepository;
import com.project.prjauth.service.UserService;
import com.project.prjauth.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserDisplayRepository userDisplayRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsResponse createUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists!");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalStateException("Username already exists!");
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findByName(PredefinedRole.USER_ROLE).ifPresent(roles::add);
        user.setRoles(roles);

        user = userRepository.save(user);
        return userMapper.toUserDetailsResponse(user);
    }

    @Override
    public UserDetailsResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found!"));
        userMapper.updateUser(user, request);
        return userMapper.toUserDetailsResponse(userRepository.save(user));
    }

    @Override
    public UserDetailsResponse updateUser(UserUpdateRequest request) {
        Long currentUserId = AuthenticationUtil.getUserId();
        if (currentUserId == null) {
            throw new ResourceNotFoundException("Cound not found current user!");
        }
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + currentUserId + " not found!"));

        userMapper.updateUser(user, request);
        return userMapper.toUserDetailsResponse(userRepository.save(user));
    }

    @Override
    public void updatePassword(PasswordUpdateRequest request) {
        User user = userRepository.findById(AuthenticationUtil.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        // Check the old password if correct
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Old password is incorrect");
        }

        // Ensure the new password is different from the old password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from old password");
        }

        // Encode and save the new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        String userEmail = AuthenticationUtil.getCurrentUserEmail();
        if (userEmail == null) {
            throw new ResourceNotFoundException("User not found!");
        }
        return userDisplayRepository.findUserProfileByEmail(userEmail);
    }

    @Override
    public UserProfileResponse getUserProfileById(Long id) {
        UserProfileResponse profile = userDisplayRepository.findUserProfileById(id);
        if (profile == null) {
            throw new ResourceNotFoundException("User not found!");
        };
        return profile;
    }

    @Override
    public UserDetailsResponse getUserDetailsById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        return userMapper.toUserDetailsResponse(user);
    }

    @Override
    public Page<UserDetailsResponse> getUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toUserDetailsResponse);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found!"));
        userRepository.deleteById(id);
    }
}
