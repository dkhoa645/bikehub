package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.ListingImage;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingResponse {
    UUID id;
    BrandResponse brandResponse;
    String title;
    String description;
    Integer usageDuration;
    String frameNumber;
    InspectionStatus inspectionStatus;
    BigDecimal price;
}
