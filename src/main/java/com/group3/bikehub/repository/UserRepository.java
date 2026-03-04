package com.group3.bikehub.repository;

import com.group3.bikehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByVerificationToken(String verificationToken);

    @Query("""
    SELECT DISTINCT u FROM User u
    JOIN u.roles r
    WHERE r.name = 'INSPECTOR'
    AND NOT EXISTS (
    SELECT i FROM Inspection i
    WHERE i.inspector = u
    AND i.scheduledAt = :scheduleAt
    )
    """)
    List<User> findAvailableInspectors(Date scheduleAt);

    boolean existsByUsername(String username);
}
