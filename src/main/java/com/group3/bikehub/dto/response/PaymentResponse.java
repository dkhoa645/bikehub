package com.group3.bikehub.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.PaymentType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    Date paidAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    Date createAt;

}
