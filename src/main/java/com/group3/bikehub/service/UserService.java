package com.group3.bikehub.service;

import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.UserMapper;
import com.group3.bikehub.repository.KycRepository;
import com.group3.bikehub.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    CurentUserService curentUserService;
    KycRepository kycRepository;


    public UserResponse getMyInfo() {
        User user = curentUserService.getCurrentUser();
        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setKyc(!ObjectUtils.isEmpty(kycRepository.findByUser(user)));
        return userResponse;
    }



}
