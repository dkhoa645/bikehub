package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.PaymentType;
import com.group3.bikehub.entity.Enum.ReferenceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentFilterRequest {
    @Builder.Default
    int page = 1;

    @Builder.Default
    int size = 10;

    PaymentStatus status;

    Date startDate;

    Date endDate;
}
