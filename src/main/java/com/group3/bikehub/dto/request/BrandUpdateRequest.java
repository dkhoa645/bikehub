package com.group3.bikehub.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandUpdateRequest {
    @NotNull(message = "FIELD_BLANK")
    Long id;
    @NotBlank(message = "FIELD_BLANK")
    String name;
}
