package com.group3.bikehub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    }


