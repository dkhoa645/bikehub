package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.RegisterRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    String verificationToken;
    String password;
    String fullName;
    RegisterRole role;
}
