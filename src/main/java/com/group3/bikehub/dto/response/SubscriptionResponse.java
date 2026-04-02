package com.group3.bikehub.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    Date createdDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    Date startDate;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    Date expiredDate;
}
