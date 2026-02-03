package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.entity.Listing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ListingMapper {
    @Mapping(target = "brand", ignore = true)
    Listing toListing(ListingCreationRequest request);

    ListingResponse toListingResponse(Listing listing);
}
