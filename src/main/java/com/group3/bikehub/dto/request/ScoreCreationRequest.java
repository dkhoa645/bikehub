package com.group3.bikehub.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoreCreationRequest {
    Long componentId;
    @Min(value = 1, message = "SCORE_MIN")
    @Max(value = 10, message = "SCORE_MAX")
    Integer score;
}
