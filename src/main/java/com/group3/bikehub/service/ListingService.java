package com.group3.bikehub.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.response.ListingImageResponse;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.dto.response.ListingSellResponse;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionMapper;
import com.group3.bikehub.mapper.ListingMapper;
import com.group3.bikehub.repository.BrandRepository;
import com.group3.bikehub.repository.ListingImagineRepository;
import com.group3.bikehub.repository.ListingRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingService {
    ListingRepository listingRepository;
    ListingMapper listingMapper;
    BrandRepository brandRepository;
    CurrentUserService currentUserService;
    CloudinaryService cloudinaryService;
    ListingImagineRepository  listingImagineRepository;



    @Transactional
    public ListingResponse createListing(ListingCreationRequest request) {

        Listing listing = listingMapper.toListing(request);

        List<Listing> list = listingRepository.findByFrameNumber(request.getFrameNumber());

        if(!list.isEmpty()){
            list.forEach(each -> {
                if(!each.getStatus().equals(ListingStatus.SOLD)){
                    throw new AppException(ErrorCode.LISTING_SOLD);
                }
            });
        }
        listing.setBrand(
                brandRepository.findByName(request.getBrandName())
                        .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED)));

        listing.setSeller(currentUserService.getCurrentUser());

        listing.setStatus(ListingStatus.DRAFT);

        listing.setCreatedAt(new Date());

        Listing listingSaved = listingRepository.save(listing);

        List<ListingImage> imageList = new ArrayList<>();

        Set<String> uniqueImages = new HashSet<>();

        for(MultipartFile file: request.getImages()){

            String key = file.getOriginalFilename();

            if(!uniqueImages.add(key)){
                throw new AppException(ErrorCode.IMAGE_DUPLICATE);
            }
        }

        int order = 1;
        for(MultipartFile file : request.getImages()) {
            try {

                Map res = cloudinaryService.uploadFile(file,"listing");
                imageList.add(listingImagineRepository.save(ListingImage.builder()
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


    public PageResponse<ListingSellResponse> getSellListing(int page, int size) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.asc("priority")
        );

        Pageable pageable = PageRequest.of(page-1, size, sort);

        var pageData = listingRepository.findActiveListings(pageable,ListingStatus.LIVE,new Date());

        return PageResponse.<ListingSellResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .pageSize(pageData.getSize())
                .totalPage(pageData.getTotalPages())
                .data(pageData.getContent().stream().map(listingMapper::toListingSellResponse).toList())
                .build();
    }

    public List<ListingSellResponse> getAllListing() {
        return listingRepository.findAll().stream()
                .map(listingMapper::toListingSellResponse)
                .toList();
    }

    public ListingResponse getById(UUID id) {
        return listingMapper.toListingResponse(
                listingRepository.findById(id)
                        .orElseThrow(()-> new AppException(ErrorCode.IMAGE_DUPLICATE))
        );
    }
}
