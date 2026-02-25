package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.InspectionLocationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationCreationRequest {

    String contactName;
    String contactPhone;
    String addressLine;
}
