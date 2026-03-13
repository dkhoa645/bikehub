package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.BankCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    UUID id;
    String nameContact;
    String phoneContact;
    String addressLine;
    String accountNumber;
    BankCode bankCode;
}
