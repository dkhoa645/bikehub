package com.group3.bikehub.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeRequest {
    @NotBlank(message = "FIELD_BLANK")
    String currentPassword;
    @NotBlank(message = "FIELD_BLANK")
    String newPassword;
}
