package com.group3.bikehub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationUpdateRequest {
    @NotBlank(message = "FIELD_BLANK")
    String contactName;
    @Pattern(
            regexp = "0[0-9]{9,10}",
            message = "PHONE_INVALID"
    )
    String contactPhone;
    @NotBlank(message = "FIELD_BLANK")
    @Size(min=5,max=2000,message = "ADDRESS_INVALID")
    String addressLine;
}
