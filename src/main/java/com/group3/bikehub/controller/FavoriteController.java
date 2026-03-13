package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.FavoriteCreationRequest;
import com.group3.bikehub.dto.response.FavoriteResponse;
import com.group3.bikehub.entity.Favorite;
import com.group3.bikehub.service.FavoriteService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteController {

    FavoriteService favoriteService;

    @PostMapping
    ApiResponse<FavoriteResponse> addFavorite(@RequestBody FavoriteCreationRequest favoriteCreationRequest) {
        return ApiResponse.<FavoriteResponse>builder()
                .result(favoriteService.createFavorite(favoriteCreationRequest))
                .build();
    }

    @GetMapping("/my-favorite")
    ApiResponse<List<FavoriteResponse>> myFavorite(){
        return ApiResponse.<List<FavoriteResponse>>builder()
                .result(favoriteService.getMyFavorite())
                .build();
    }

    @DeleteMapping("/{listingId}")
    ApiResponse<Void> deleteFavorite(@PathVariable("listingId") UUID listingId){
        favoriteService.deleteFavorite(listingId);
        return ApiResponse.<Void>builder()
                .message("Favorite deleted")
                .build();
    }

}
