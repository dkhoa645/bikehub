package com.group3.bikehub.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanUpdateRequest {
    String name;
    String description;
    BigDecimal price;
    Integer priority;
    Integer durationDays;
}
