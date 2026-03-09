package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.InspectionImageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionImageResponse {
    String url;
    InspectionImageType type;
}
