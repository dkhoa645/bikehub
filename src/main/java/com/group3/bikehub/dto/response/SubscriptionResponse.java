package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.SubscriptionStatusEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubscriptionResponse {
    UUID id;
    UUID listingId;
    PlanResponse plan;
    SubscriptionStatusEnum status;
    Date createdDate;
    Date startDate;
    Date expiredDate;
}
