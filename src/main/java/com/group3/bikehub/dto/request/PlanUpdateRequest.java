package com.group3.bikehub.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanUpdateRequest {
    @NotBlank(message = "FIELD_BLANK")
    String name;
    @Size(min=1,max=100,message = "DESCRIPTION_MAX")
    String description;
    @DecimalMin(value = "1000", message = "PRICE_MIN")
    @DecimalMax(value = "10000000", message = "PRICE_MAX")
    @NotNull
    @Digits(integer = 10, fraction = 0)
    BigDecimal price;
    @NotBlank
    @Min(value = 1,message = "DURATION_DAY_MIN")
    Integer priority;
    @Min(value = 1,message = "DURATION_DAY_MIN")
    Integer durationDays;
}
