package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.ListingImage;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingResponse {
    BrandResponse brandResponse;
    String tittle;
    String description;
    Integer usageDuration;
    String frameNumber;
    BigDecimal price;


}
