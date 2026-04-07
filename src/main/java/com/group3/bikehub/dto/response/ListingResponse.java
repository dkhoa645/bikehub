package com.group3.bikehub.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Subscription;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListingResponse {
    UUID id;
    BrandResponse brand;
    String title;
    String description;
    Integer manufactureYear;
    String frameNumber;
    ListingStatus status;
    BigDecimal price;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    Date createdAt;
    List<ListingImageResponse> images;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    Date expiryAt;
    List<SubscriptionResponse> subscriptions;

}
