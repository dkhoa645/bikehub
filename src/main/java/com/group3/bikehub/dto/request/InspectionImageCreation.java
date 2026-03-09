package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.InspectionImageType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectionImageCreation {
    InspectionImageType type;
    MultipartFile file;
}
