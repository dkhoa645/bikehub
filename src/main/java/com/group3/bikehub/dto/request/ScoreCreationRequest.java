package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.InspectionImageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoreCreationRequest {
    @Schema(description = "Chụp các type gồm LEFT_VIEW, RIGHT_VIEW,FRONT_VIEW,REAR_VIEW")
    List<InspectionImageCreation> inspectionImageCreations;
    String comment;
    Integer score;

}
