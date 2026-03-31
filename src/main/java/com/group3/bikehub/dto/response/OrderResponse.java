package com.group3.bikehub.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.SellerStatus;
import jakarta.persistence.OneToOne;
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
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    Date createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
    Date expiresAt;
    OrderLocationResponse orderLocation;
}
