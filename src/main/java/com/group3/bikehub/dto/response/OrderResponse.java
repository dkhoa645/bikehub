package com.group3.bikehub.dto.response;

import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.SellerStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    UUID id;
    UserResponse buyer;
    UserResponse seller;
    SellerStatus sellerStatus;
    OrderStatus orderStatus;
    ListingResponse listing;
    Date createdAt;
    Date expiresAt;
}
