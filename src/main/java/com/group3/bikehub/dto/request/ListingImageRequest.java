package com.group3.bikehub.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingImageRequest {
    MultipartFile overview1;
    MultipartFile overview2;
    MultipartFile overview3;

}
