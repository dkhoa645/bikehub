package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.SubscriptionResponse;
import com.group3.bikehub.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionResponse toSubscriptionResponse(Subscription subscription);
}
