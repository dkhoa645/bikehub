package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.AddressCreationRequest;
import com.group3.bikehub.dto.request.AddressUpdateRequest;
import com.group3.bikehub.dto.response.AddressResponse;
import com.group3.bikehub.entity.Address;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressCreationRequest request);
    AddressResponse toAddressResponse(Address address);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddress(AddressUpdateRequest request, @MappingTarget Address address);
}
