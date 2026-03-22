package com.group3.bikehub.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KycResponse {
    String idNumber;
    String fullName;
    String dateOfBirth;
    String gender;
    String nationality;
    String placeOfOrigin;
    String placeOfResidence;
    String expiryDate;
}