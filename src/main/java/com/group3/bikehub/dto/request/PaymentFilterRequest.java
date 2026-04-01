package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.PaymentType;
import com.group3.bikehub.entity.Enum.ReferenceType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    Date endDate;
}
