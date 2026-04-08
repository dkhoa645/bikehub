package com.group3.bikehub.service;

import ch.qos.logback.core.util.StringUtil;
import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.request.ListingUpdateRequest;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.dto.response.ListingSellResponse;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.ListingMapper;
import com.group3.bikehub.repository.BrandRepository;
import com.group3.bikehub.repository.FavoriteRepository;
import com.group3.bikehub.repository.ListingImageRepository;
import com.group3.bikehub.repository.ListingRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ListingService {
    ListingRepository listingRepository;
    ListingMapper listingMapper;
    BrandRepository brandRepository;
    CurrentUserService currentUserService;
    CloudinaryService cloudinaryService;
    ListingImageRepository listingImageRepository;
    FavoriteRepository favoriteRepository;


    @Transactional
    public ListingResponse createListing(ListingCreationRequest request) throws IOException {

        User user = currentUserService.getCurrentUser();

        if (request.getFrameNumber() != null && !request.getFrameNumber().isBlank()){
            throw new AppException(ErrorCode.ADDRESS_NOT_REGISTERED);
        }

        if(request.getFrameNumber() != null){
        listingRepository.findAll().forEach(listing -> {
            if(listing.getFrameNumber().equals(request.getFrameNumber())){
                throw new AppException(ErrorCode.INVALID_FRAME_NUMBER);
                }
            });
        }

        Listing listing = listingMapper.toListing(request);

        List<Listing> list = listingRepository.findByFrameNumber(request.getFrameNumber());

        if(!list.isEmpty()){
            list.forEach(each -> {
                if(!each.getStatus().equals(ListingStatus.SOLD) &&
                        !each.getStatus().equals(ListingStatus.DELETED)&&
                        !each.getStatus().equals(ListingStatus.EXPIRED) ){
                    throw new AppException(ErrorCode.LISTING_EXIST);
                }
            });
        }

        listing.setBrand(
                brandRepository.findByName(request.getBrandName())
                        .orElseThrow(()->new AppException(ErrorCode.BRAND_NOT_FOUND)));

        listing.setSeller(currentUserService.getCurrentUser());

        listing.setStatus(ListingStatus.DRAFT);

        listing.setCreatedAt(new Date());

        Listing listingSaved = listingRepository.save(listing);

        List<ListingImage> imageList = new ArrayList<>();

        Set<String> uniqueImages = new HashSet<>();

        for (MultipartFile file : request.getImages()) {
            String key = DigestUtils.sha256Hex(file.getBytes());
            if (!uniqueImages.add(key)) {
                throw new AppException(ErrorCode.IMAGE_DUPLICATE);
            }
        }

        int order = 1;
        for(MultipartFile file : request.getImages()) {
            try {
                Map res = cloudinaryService.uploadFile(file,"listing");
                imageList.add(listingImageRepository.save(ListingImage.builder()
                        .secureUrl((String)res.get("secure_url"))
                        .listing(listingSaved)
                        .imageOrder(order)
                        .build()));
                order++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return listingMapper.toListingResponse(listingSaved);
    }




    public List<ListingResponse> getMyListing() {
        User user = currentUserService.getCurrentUser();
        List<Listing> list = listingRepository.findBySellerOrderByCreatedAtDesc(user);
        return list.stream()
                .map(listingMapper::toListingResponse)
                .toList();
    }


    public PageResponse<ListingSellResponse> getSellListing(
            int page, int size, Long brandId,
            BigDecimal minPrice, BigDecimal maxPrice) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.asc("priority")
        );

        Pageable pageable = PageRequest.of(page-1, size, sort);

        var pageData = listingRepository.findActiveListings(
                pageable,ListingStatus.LIVE,new Date(), brandId,minPrice,maxPrice);

        return PageResponse.<ListingSellResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .totalPage(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(listingMapper::toListingSellResponse)
                        .toList())
                .build();
    }

    public List<ListingSellResponse> getAllListing() {
        return listingRepository.findAll().stream()
                .map(listingMapper::toListingSellResponse)
                .toList();
    }

    public ListingResponse getBySellerId(UUID id) {
        return listingMapper.toListingResponse(
                listingRepository.findById(id)
                        .orElseThrow(()-> new AppException(ErrorCode.IMAGE_DUPLICATE))
        );
    }

    public ListingSellResponse getById(UUID id) {
        User user = currentUserService.getCurrentUser();
        List<Favorite>  favorites = favoriteRepository.findByUser(user);

        Listing listing = listingRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));

        ListingSellResponse listingSellResponse = listingMapper.toListingSellResponse(listing);

        favorites.forEach(each -> {
            if(each.getListing().equals(listing)){
                listingSellResponse.setFavorite(true);
            }});

        return listingSellResponse;
    }

    public void deleteListing(UUID id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));
        listing.setStatus(ListingStatus.DELETED);
        listing.setExpiryAt(new Date());
        listingRepository.save(listing);
    }

    public ListingResponse updateListing(UUID id, ListingUpdateRequest request) {
        Brand brand = brandRepository.findByName(request.getBrandName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));
        Listing listing = listingRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));

        listingMapper.updateListing(request, listing);

        List<Listing> list = listingRepository.findByFrameNumber(request.getFrameNumber());

        boolean existsInvalidListing = list.stream().anyMatch(each ->
                !each.getStatus().equals(ListingStatus.SOLD) &&
                        !each.getStatus().equals(ListingStatus.DELETED) &&
                        !each.getStatus().equals(ListingStatus.EXPIRED)
        );

        if (existsInvalidListing) {
            throw new AppException(ErrorCode.LISTING_EXIST);
        }

        int totalImages = request.getImages().size();
        if (totalImages > 3 ) {
            throw new AppException(ErrorCode.IMAGE_LIMIT);
        }else {
            List<ListingImage> toKeep = listing.getImages();
            List<UUID> toDelete = new ArrayList<>();
            request.getImages().forEach(image -> {
                ListingImage listingImage = listingImageRepository.findBySecureUrl(image.getUrl())
                        .orElse(null);
                if(listingImage != null){
                    listingImage.setImageOrder(image.getOrder());
                }else{
                    listingImage = ListingImage.builder()
                            .imageOrder(image.getOrder())
                            .secureUrl(image.getUrl())
                            .build();
                }
                listingImageRepository.save(listingImage);
            });
        }
        return listingMapper.toListingResponse(listingRepository.save(listing));
    }
}
