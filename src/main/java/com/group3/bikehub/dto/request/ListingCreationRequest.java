package com.group3.bikehub.dto.request;

import com.group3.bikehub.dto.response.BrandResponse;
import com.group3.bikehub.entity.Enum.BikeType;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Brand được tạo bằng API brand", example = "/brand", requiredMode = Schema.RequiredMode.REQUIRED)
    String brandName;
    @Schema(description = "Loại xe", example = "MTP_BIKE, ROAD_BIKE", requiredMode = Schema.RequiredMode.REQUIRED)
    BikeType bikeType;
    String title;
    @Schema(description = "Thời gian đã sử dụng (năm)", example = "12")
    Integer usageDuration;
    @Schema(description = "FRAME_NUMBER ko được trùng, trừ khi đã SOLD", example = "FN-ABC-123456", requiredMode = Schema.RequiredMode.REQUIRED)
    String frameNumber;
    String description;
    BigDecimal price;
    @Size(min = 3, max = 8)
    List<MultipartFile> images;
}
