package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.InspectionLocationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionLocationResponse {
    UUID id;
    InspectionLocationType inspectionType;
    String contactName;
    String contactPhone;
    String addressLine;
}
