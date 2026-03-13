package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.FavoriteCreationRequest;
import com.group3.bikehub.dto.response.FavoriteResponse;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Favorite;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.FavoriteMapper;
import com.group3.bikehub.repository.FavoriteRepository;
import com.group3.bikehub.repository.ListingRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteService {
    FavoriteRepository favoriteRepository;
    CurrentUserService currentUserService;
    ListingRepository listingRepository;
    FavoriteMapper favoriteMapper;

    public FavoriteResponse createFavorite(FavoriteCreationRequest favoriteCreationRequest) {
        User user = currentUserService.getCurrentUser();

        Listing listing = listingRepository.findById(favoriteCreationRequest.getListingId())
                .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));

        if(favoriteRepository.existsByUserAndListing(user,listing)) {
            throw new AppException(ErrorCode.FAVORITE_DUPLICATE);
        }

        if(!listing.getStatus().equals(ListingStatus.LIVE)){
            throw new AppException(ErrorCode.LISTING_NOT_FOUND);
        }

        Favorite favorite = favoriteRepository.save(Favorite.builder()
                        .listing(listing)
                        .user(user)
                        .createdAt(new Date())
                        .build());

        return favoriteMapper.toResponse(favorite);
    }

    public List<FavoriteResponse> getMyFavorite() {
        User user = currentUserService.getCurrentUser();
        List<Favorite> list = favoriteRepository.findByUser(user);
        return list.stream()
                .map(favoriteMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteFavorite(UUID listingId) {
        User user = currentUserService.getCurrentUser();
        Listing listing = listingRepository.findById(listingId).
                orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));

        favoriteRepository.deleteByListingAndUser(listing,user);
        }


    }

