package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.InspectorCreationRequest;
import com.group3.bikehub.dto.request.UserCreationRequest;
import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.entity.Address;
import com.group3.bikehub.entity.Enum.RoleEnum;
import com.group3.bikehub.entity.Kyc;
import com.group3.bikehub.entity.Role;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
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

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    SendGridService sendGridService;

    public UserResponse getMyInfo() {
        User user = currentUserService.getCurrentUser();
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setHasAddress(!ObjectUtils.isEmpty(user.getAddress()));
        userResponse.setKyc(!ObjectUtils.isEmpty(kycRepository.findByUser(user)));
        return userResponse;
    }


    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }



    public UserResponse createInspector(InspectorCreationRequest inspectorCreationRequest) {
        String subject = "Inspector Account Credentials";
        String body = """
        <div style="font-family: Arial, sans-serif; line-height: 1.6">
          <h2>Inspector Account Created</h2>
          <p>Hello <b>%s</b>,</p>
        
          <p>An inspector account has been created for you </p>
        
          <div style="padding: 12px; border: 1px solid #e5e7eb; border-radius: 8px; background: #f9fafb">
            <p style="margin: 0"><b>Username:</b> <span style="font-family: monospace">%s</span></p>
            <p style="margin: 0"><b>Temporary Password:</b> <span style="font-family: monospace">%s</span></p>
          </div>
        
          <p style="margin-top: 12px">
            For security, please <b>log in and change your password immediately</b>.
          </p>
        
          <p>
            If you did not expect this email, please contact the administrator.
          </p>
        
          <hr style="border: none; border-top: 1px solid #e5e7eb; margin: 16px 0" />
          <p style="color: #6b7280; font-size: 12px">
            This is an automated message. Please do not reply.
          </p>
        </div>
""".formatted(
                inspectorCreationRequest.getName(),
                inspectorCreationRequest.getUsername(),
                inspectorCreationRequest.getPassword()
        );

        if(userRepository.existsByUsername(inspectorCreationRequest.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        try {
            sendGridService.dispatchEmail(
                    inspectorCreationRequest.getUsername(),
                    subject,
                    body
            );
        } catch (IOException e) {
            throw new AppException(ErrorCode.SEND_EMAIL_FAILED);
        }
        Role role = roleRepository.findByName(RoleEnum.INSPECTOR.name())
                .orElseThrow();
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        return userMapper.toUserResponse(
                userRepository.save(
                User.builder()
                        .username(inspectorCreationRequest.getUsername())
                        .password(passwordEncoder.encode(inspectorCreationRequest.getPassword()))
                        .roles(roles)
                        .name(inspectorCreationRequest.getName())
                        .build()
                ));
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

    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
