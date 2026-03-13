package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.request.FavoriteCreationRequest;
import com.group3.bikehub.dto.response.FavoriteResponse;
import com.group3.bikehub.entity.Favorite;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {
    FavoriteResponse toResponse(Favorite favorite);
}
