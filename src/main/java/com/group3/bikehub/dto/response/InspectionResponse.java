package com.group3.bikehub.dto.response;

import com.group3.bikehub.dto.request.ScoreCreationRequest;
import com.group3.bikehub.entity.Enum.InspectionResult;
import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Enum.InspectionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionResponse {
    UUID inspectionId;
    InspectionType inspectionType;
    InspectionLocationResponse location;
    InspectionStatus status;
    UserResponse inspector;
    InspectionResult inspectionResult;
    Date scheduledAt;
    Date expiredAt;
    Date createdAt;
    int score;
    String comment;
    List<InspectionImageResponse> images;
}
