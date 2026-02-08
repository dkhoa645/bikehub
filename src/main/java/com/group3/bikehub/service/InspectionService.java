package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.InspectionCreationRequest;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.entity.Enum.InspectionLocationType;
import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Enum.InspectionType;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.entity.Inspection;
import com.group3.bikehub.entity.InspectionLocation;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionLocationMapper;
import com.group3.bikehub.mapper.InspectionMapper;
import com.group3.bikehub.repository.InspectionLocationRepository;
import com.group3.bikehub.repository.InspectionRepository;
import com.group3.bikehub.repository.ListingRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionService {
    InspectionRepository inspectionRepository;
    InspectionMapper inspectionMapper;
    ListingRepository listingRepository;
    InspectionLocationRepository inspectionLocationRepository;
    InspectionLocationMapper inspectionLocationMapper;
    CurrentUserService currentUserService;


    @Transactional
    public InspectionResponse createInspection(InspectionCreationRequest request) {
        Inspection inspection = inspectionMapper.toInspection(request);
        inspection.setStatus(InspectionStatus.PENDING_ASSIGNED);

        Listing listing = listingRepository.findById(request.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.DRAFT_NOT_FOUND));
        listing.setStatus(ListingStatus.PENDING);
        inspection.setListing(listing);
        inspection.setCreatedAt(new Date());
        InspectionLocation location = null;
        if (request.getInspectionType().equals(InspectionType.ONSITE)) {
            User user = currentUserService.getCurrentUser();
            location = inspectionLocationRepository.save(
                    inspectionLocationMapper.fromAddress(user.getAddress())
            );
        } else{
            location = inspectionLocationRepository.findById(request.getInspectionLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));
        }
        inspection.setLocation(location);


        return inspectionMapper.toInspectionResponse(inspectionRepository.save(inspection));
    }
}
