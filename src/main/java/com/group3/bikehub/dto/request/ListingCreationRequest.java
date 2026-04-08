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
    @NotNull
    BikeType bikeType;
    @Size(min=1,max=100, message = "TITLE_MAX")
    @Schema(description = "Max 100 ki tu", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min=1,max=50, message = "TITLE_MAX")
    String title;
    @Schema(description = "Năm sản xuất min 2000, max 2026")
    @Max(value = 2026, message = "DURATION_MAX")
    @Min(value = 2000, message = "DURATION_MAX")
    Integer manufactureYear;
    @Schema(description = "FRAME_NUMBER ko được trùng, trừ khi đã SOLD")
    String frameNumber;
    @NotBlank
    @Size(max = 2000, message = "DESCRIPTION_MAX")
    String description;
    @DecimalMin(value = "100000", message = "PRICE_MIN")
    @DecimalMax(value = "10000000", message = "PRICE_MAX")
    @Schema(description = "Giá bán min 100k, max 100m ")
    @NotNull
    @Digits(integer = 10, fraction = 0)
    BigDecimal price;
    @Size(min = 1, max = 3)
    @Schema(description = "Tối thiểu 1, tối đa 3 ")
    List<MultipartFile> images;
}
