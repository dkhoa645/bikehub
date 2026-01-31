package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Enum.KycStatus;
import com.group3.bikehub.entity.Kyc;
import com.group3.bikehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KycRepository extends JpaRepository<Kyc, String> {
    boolean existsByUser(User user);
    List<Kyc> findAll();
    List<Kyc> findByUser(User user);
    boolean existsByUserAndStatus(User user, KycStatus status);
    Optional<Kyc> findByUserAndStatus(User user, KycStatus status);

}
