package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.ListingCreationRequest;
import com.group3.bikehub.dto.response.ListingResponse;
import com.group3.bikehub.entity.Brand;
import com.group3.bikehub.entity.Enum.ListingImageType;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.ListingImage;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingService {
    ListingRepository listingRepository;
    ListingMapper listingMapper;
    BrandRepository brandRepository;
    CurentUserService curentUserService;
    CloudinaryService cloudinaryService;
    ListingImagineRepository  listingImagineRepository;



    @Transactional
    public ListingResponse createListing(ListingCreationRequest request) {

        Listing listing = listingMapper.toListing(request);

        listing.setBrand(
                brandRepository.findByName(request.getBrandName())
                        .orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED)));

        listing.setSeller(curentUserService.getCurrentUser());

        listing.setStatus(ListingStatus.PENDING);

        listing.setCreatedAt(new Date());

        Listing listingSaved = listingRepository.save(listing);

        for(MultipartFile file : request.getImages()) {
            try {
                int order = 0;
                Map res = cloudinaryService.uploadFile(file,"listing");
                listingImagineRepository.save(ListingImage.builder()
                        .secureUrl((String)res.get("secure_url"))
                        .listing(listingSaved)
                                .imageOrder(order+1)
                        .build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        return listingMapper.toListingResponse(listingSaved);
    }

}
