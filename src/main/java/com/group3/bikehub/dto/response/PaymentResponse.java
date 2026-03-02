package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.PaymentType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentResponse {
    Long paymentId;
    PaymentType type;
    String referenceId;
    BigDecimal amount;
    PaymentStatus status;
    String transactionRef;
    Long payosOrderCode;
    LocalDateTime paidAt;
}
