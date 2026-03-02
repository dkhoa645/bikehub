package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.InspectorCreationRequest;
import com.group3.bikehub.dto.request.UserCreationRequest;
import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.entity.Role;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.mapper.UserMapper;
import com.group3.bikehub.repository.KycRepository;
import com.group3.bikehub.repository.RoleRepository;
import com.group3.bikehub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    CurrentUserService currentUserService;
    KycRepository kycRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse getMyInfo() {
        User user = currentUserService.getCurrentUser();
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setKyc(!ObjectUtils.isEmpty(kycRepository.findByUser(user)));
        return userResponse;
    }


    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse createInspector(InspectorCreationRequest inspectorCreationRequest) {
        return userMapper.toUserResponse(
                User.builder()
                        .username(inspectorCreationRequest.getUsername())
                        .password(passwordEncoder.encode(inspectorCreationRequest.getPassword()))
                        .build()
        );
    }

    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        User user = userMapper.toUser(userCreationRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(userCreationRequest.getRole().name())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(role);
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
