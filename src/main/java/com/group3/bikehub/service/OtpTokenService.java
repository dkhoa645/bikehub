package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.OtpTokenRequest;
import com.group3.bikehub.dto.request.OtpVerifyRequest;
import com.group3.bikehub.dto.response.OtpTokenResponse;
import com.group3.bikehub.dto.response.OtpVerifyResponse;
import com.group3.bikehub.entity.OtpToken;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.repository.OtpTokenRepository;
import com.group3.bikehub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtpTokenService {
    OtpTokenRepository otpTokenRepository;
    UserRepository userRepository;
    private static final SecureRandom secureRandom = new SecureRandom();
    SendGridService sendGridService;

    public static String generateOtp() {
        int otp = secureRandom.nextInt(1_000_000);
        return String.format("%06d", otp);
    }


    public OtpTokenResponse sendRegistrationOtp(OtpTokenRequest otpRequest) {

        User user = userRepository.findByUsername(otpRequest.getEmail())
                .orElse(null);
        if (user != null || user.isVerified()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Date exp = new Date(Instant.now().plus(5, ChronoUnit.MINUTES)
                .toEpochMilli());

        String otp = generateOtp();

        String subject = "Your OTP Code";
        String body = """
        <div style="font-family: Arial, sans-serif">
            <h2>OTP Verification</h2>
            <p>Your OTP code is:</p>
            <h1 style="color:#2e6cf6">%s</h1>
            <p>This code will expire in <b>5 minutes</b>.</p>
            <br/>
            <p>If you did not request this code, please ignore this email.</p>
        </div>
    """.formatted(otp);

        try {
            sendGridService.dispatchEmail(
                    otpRequest.getEmail(),
                    subject,
                    body
            );
        } catch (IOException e) {
            throw new AppException(ErrorCode.SEND_EMAIL_FAILED);
        }


        otpTokenRepository.save(OtpToken.builder()
                        .otp(otp)
                        .expiration(exp)
                        .mail(otpRequest.getEmail())
                        .build());

        return OtpTokenResponse.builder()
                .otp(otp)
                .build();
    }

    @Transactional
    public OtpVerifyResponse verifyOtp(OtpVerifyRequest otpRequest) {
        OtpToken otpToken = otpTokenRepository.findByOtp(otpRequest.getOtp())
                .orElseThrow( () -> new AppException(ErrorCode.INVALID_TOKEN));
        Date expOtp = otpToken.getExpiration();
        if (!expOtp.after(new Date())) {
           throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String verificationToken = UUID.randomUUID().toString();
        Date expiration = new Date(Instant.now().plus(15, ChronoUnit.MINUTES)
                .toEpochMilli());
        User user = userRepository.findByUsername(otpToken.getMail())
                .orElseGet(()-> {
                    User newUser = new User();
                    newUser.setUsername(otpToken.getMail());
                    return newUser;
                });
        user.setVerificationToken(verificationToken);
        user.setExpiration(expiration);
        otpTokenRepository.delete(otpToken);
        userRepository.save(user);

        return OtpVerifyResponse.builder().verificationToken(verificationToken).build();
    }
}
