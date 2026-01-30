package com.group3.bikehub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kyc {
    @Id
    private String idNumber;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String nationality;
    private String placeOfOrigin;
    private String placeOfResidence;
    private String expiryDate;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}


