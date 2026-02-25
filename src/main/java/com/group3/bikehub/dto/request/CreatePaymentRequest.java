package com.group3.bikehub.dto.request;

import com.group3.bikehub.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreatePaymentRequest {
    Long order_id;
    String description;

}
