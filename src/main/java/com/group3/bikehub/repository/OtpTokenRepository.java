package com.group3.bikehub.repository;

import com.group3.bikehub.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken,String> {
    Optional<OtpToken> findByOtp(String otp);
}
