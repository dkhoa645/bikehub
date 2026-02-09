package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.response.ListingImageResponse;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.ListingImage;
import com.group3.bikehub.entity.User;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    InspectionMapper inspectionMapper;


    @Transactional
    public ListingResponse createListing(ListingCreationRequest request) {

        Listing listing = listingMapper.toListing(request);

        List<Listing> list = listingRepository.findByFrameNumber(request.getFrameNumber());
        if(!list.isEmpty()){
            list.forEach(each -> {
                if(!listing.getStatus().equals(ListingStatus.SOLD)){
                    throw new AppException(ErrorCode.LISTING_STATUS);
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
        List<Listing> list = listingRepository.findBySeller(user);
        return list.stream()
                .map(listingMapper::toListingResponse)
                .toList();
    }
}
