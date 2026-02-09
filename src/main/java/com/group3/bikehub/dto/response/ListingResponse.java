package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Enum.ListingStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingResponse {
    UUID id;
    BrandResponse brand;
    String title;
    String description;
    Integer usageDuration;
    String frameNumber;
    ListingStatus status;
    BigDecimal price;
    Date createdAt;
    List<ListingImageResponse> images;
}
