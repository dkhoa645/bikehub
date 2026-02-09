package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.ListingImageResponse;
import com.group3.bikehub.entity.ListingImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ListingImageMapper {
    ListingImageResponse toResponse(ListingImage listingImage);
}
