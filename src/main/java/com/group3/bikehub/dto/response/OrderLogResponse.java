package com.group3.bikehub.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderLogResponse {
    Long id;
    OrderStatus status;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    Date createdAt;
    String image;
}
