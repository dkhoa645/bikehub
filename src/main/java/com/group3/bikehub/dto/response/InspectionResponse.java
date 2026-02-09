package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.InspectionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionResponse {
    InspectionType type;
    UUID inspectionLocationId;
    UUID listingId;
    Long locationId;
    Date scheduledAt;
}
