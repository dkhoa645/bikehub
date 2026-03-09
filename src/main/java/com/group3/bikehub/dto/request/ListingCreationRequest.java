package com.group3.bikehub.dto.request;

import com.group3.bikehub.dto.response.BrandResponse;
import com.group3.bikehub.entity.Enum.BikeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
    @Schema(description = "Brand được tạo bằng API brand", requiredMode = Schema.RequiredMode.REQUIRED)
    String brandName;
    @Schema(description = "Loại xe MTB_BIKE, ROAD_BIKE", requiredMode = Schema.RequiredMode.REQUIRED)
    BikeType bikeType;
    String title;
    @Schema(description = "Thời gian đã sử dụng (năm) min 0, max 10")
    @Max(10)
    @Min(0)
    Integer usageDuration;
    @Schema(description = "FRAME_NUMBER ko được trùng, trừ khi đã SOLD",  requiredMode = Schema.RequiredMode.REQUIRED)
    String frameNumber;
    String description;
    @DecimalMin(value = "100000", message = "PRICE_MIN")
    @DecimalMax(value = "10000000")
    @Schema(description = "Giá bán min 100k, max 100m ")
    BigDecimal price;
    @Size(min = 3, max = 5)
    @Schema(description = "Tối thiểu 3, tối đa 5 ")
    List<MultipartFile> images;
}
