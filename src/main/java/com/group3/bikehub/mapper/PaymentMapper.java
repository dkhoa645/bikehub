package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.PaymentResponse;
import com.group3.bikehub.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toPaymentResponse(Payment payment);
}
