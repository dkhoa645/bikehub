package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.BankCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreationRequest {
    String nameContact;
    String phoneContact;
    String addressLine;
    String accountNumber;
    BankCode bankCode;
}
