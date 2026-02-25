package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.*;
import com.group3.bikehub.dto.response.AuthenticationResponse;
import com.group3.bikehub.dto.response.OtpVerifyResponse;
import com.group3.bikehub.service.AuthenticationService;
import com.group3.bikehub.service.CloudinaryService;
import com.group3.bikehub.service.OtpTokenService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;
    OtpTokenService otpTokenService;
    CloudinaryService cloudinaryService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout (@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest refreshRequest) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(refreshRequest))
                .build();
    }

    @PostMapping("/send-otp")
    ApiResponse<Void> sendOtp(@RequestBody OtpTokenRequest otpRequest) {
        otpTokenService.sendRegistrationOtp(otpRequest);
        return ApiResponse.<Void>builder()
                .message("OTP Sent Successfully!")
                .build();
    }

    @PostMapping("/verify-otp")
    ApiResponse<OtpVerifyResponse> verifyOtp(@RequestBody OtpVerifyRequest otpRequest)  {
        return ApiResponse.<OtpVerifyResponse>builder()
                .result(otpTokenService.verifyOtp(otpRequest))
                .build();
    }

    @PostMapping("/registration")
    ApiResponse<Void> registration(@RequestBody RegistrationRequest registrationRequest)  {
        authenticationService.registration(registrationRequest);
        return ApiResponse.<Void>builder()
                .message("Registration Successful")
                .build();
    }






}
