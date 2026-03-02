package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Enum.RoleEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    String username;
    String password;
    RoleEnum role;
}
