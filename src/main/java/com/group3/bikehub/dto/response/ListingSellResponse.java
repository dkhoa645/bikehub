package com.group3.bikehub.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ListingSellResponse {
    UUID id;
    BrandResponse brand;
    String title;
    String description;
    Integer usageDuration;
    String frameNumber;
    ListingStatus status;
    BigDecimal price;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    Date createdAt;
    List<ListingImageResponse> images;
}
