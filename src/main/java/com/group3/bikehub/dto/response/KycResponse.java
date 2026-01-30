package com.group3.bikehub.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KycResponse {
    private String idNumber;
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String nationality;
    private String placeOfOrigin;
    private String placeOfResidence;
    private String expiryDate;
}