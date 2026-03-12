package com.group3.bikehub.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.group3.bikehub.entity.Enum.TransactionStatus;
import com.group3.bikehub.entity.Enum.TransactionType;
import com.group3.bikehub.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponse {
    UUID id;
    UserResponse fromUser;
    UserResponse toUser;
    BigDecimal amount;
    TransactionStatus status;
    TransactionType type;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    Date createdAt;
}
