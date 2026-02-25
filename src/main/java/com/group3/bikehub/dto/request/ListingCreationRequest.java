package com.group3.bikehub.dto.request;

import com.group3.bikehub.dto.response.BrandResponse;
import com.group3.bikehub.entity.Enum.BikeType;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingCreationRequest {
    String brandName;
    BikeType bikeType;
    String title;
    Integer usageDuration;
    String frameNumber;
    String description;
    BigDecimal price;
    @Size(min = 3, max = 8)
    List<MultipartFile> images;
}
