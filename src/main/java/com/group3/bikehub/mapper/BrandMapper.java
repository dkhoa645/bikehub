package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.BrandCreationRequest;
import com.group3.bikehub.dto.response.BrandResponse;
import com.group3.bikehub.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandCreationRequest request);
    BrandResponse toBrandResponse(Brand brand);
}
