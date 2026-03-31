package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.BankCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCreationRequest {
    @NotBlank(message = "FIELD_BLANK")
    String nameContact;
    @Pattern(
            regexp = "0[0-9]{9}",
            message = "PHONE_INVALID"
    )
    String phoneContact;
    @NotBlank(message = "FIELD_BLANK")
    @Size(min=5,max=2000,message = "ADDRESS_INVALID")
    String addressLine;
    @NotBlank(message = "FIELD_BLANK")
    @Pattern(regexp = "\\d{8,20}", message = "ACCOUNT_INVALID")
    String accountNumber;
    BankCode bankCode;
}
