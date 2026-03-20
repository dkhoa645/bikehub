package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.request.ListingUpdateRequest;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.dto.response.ListingSellResponse;
import com.group3.bikehub.entity.Listing;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ListingMapper {

    Listing toListing(ListingCreationRequest request);

    ListingResponse toListingResponse(Listing listing);

    ListingSellResponse toListingSellResponse(Listing listing);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateListing(ListingUpdateRequest listingUpdateRequest, @MappingTarget Listing listing);
}
