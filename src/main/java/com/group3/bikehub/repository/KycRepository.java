package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KycRepository extends JpaRepository<Kyc, Long> {
    Optional<Kyc> findByIdNumber(String id);

}
