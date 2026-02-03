package com.group3.bikehub.entity;

import com.group3.bikehub.entity.Enum.KycStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kyc {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String idNumber;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String nationality;
    private String placeOfOrigin;
    private String placeOfResidence;
    private String expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
    private KycStatus status;
    private LocalDateTime verifiedAt;
    private LocalDateTime submittedAt;
}


