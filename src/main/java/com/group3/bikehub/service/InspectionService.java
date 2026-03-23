package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.InspectionAssignRequest;
import com.group3.bikehub.dto.request.InspectionCreationRequest;
import com.group3.bikehub.dto.request.InspectorAvailableRequest;
import com.group3.bikehub.dto.response.InspectionResponse;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.dto.response.UserResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.InspectionLocationType;
import com.group3.bikehub.entity.Enum.InspectionStatus;
import com.group3.bikehub.entity.Enum.InspectionType;
import com.group3.bikehub.entity.Enum.ListingStatus;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.InspectionMapper;
import com.group3.bikehub.mapper.UserMapper;
import com.group3.bikehub.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InspectionService {
    InspectionRepository inspectionRepository;
    InspectionMapper inspectionMapper;
    ListingRepository listingRepository;
    InspectionLocationRepository inspectionLocationRepository;
    CurrentUserService currentUserService;
    AddressRepository addressRepository;
    UserRepository userRepository;
    UserMapper userMapper;

    @Transactional
    public InspectionResponse createInspection(InspectionCreationRequest request) {
        Inspection inspection = inspectionMapper.toInspection(request);
        inspection.setStatus(InspectionStatus.PENDING_ASSIGNED);
        Listing listing = listingRepository.findById(request.getListingId())
                .orElseThrow(() -> new AppException(ErrorCode.DRAFT_NOT_FOUND));
        if(!listing.getStatus().equals(ListingStatus.PAID)) {
            throw new AppException(ErrorCode.LISTING_STATUS);
        }
        listing.setStatus(ListingStatus.PENDING);
        inspection.setListing(listing);
        inspection.setCreatedAt(new Date());
        inspection.setExpiredAt(Date.from(request.getScheduledAt().toInstant()
                .plus(2, ChronoUnit.HOURS)));

        if(request.getScheduledAt().before(new Date())){
            throw new AppException(ErrorCode.TIME_BEFORE);
        }

        InspectionLocation location = new InspectionLocation() ;
        if (request.getInspectionType().equals(InspectionType.ONSITE)) {
            User user = currentUserService.getCurrentUser();
            Address address = addressRepository.findByUser(user);
            location.setType(InspectionLocationType.SELLER);
            location.setContactName(address.getNameContact());
            location.setContactPhone(address.getPhoneContact());
            location.setAddressLine(address.getAddressLine());
            location = inspectionLocationRepository.save(location);
        } else{
            location = inspectionLocationRepository.findById(request.getInspectionLocationId())
                .orElseThrow(() -> new AppException(ErrorCode.LOCATION_NOT_FOUND));
        }
        inspection.setLocation(location);



        return inspectionMapper.toInspectionResponse(inspectionRepository.save(inspection));
    }

    public InspectionResponse getInspection(UUID listingId) {
        Listing listing = listingRepository.findById(listingId).orElse(null);
        Inspection inspection = new Inspection();
        if (listing != null) {
            inspection = inspectionRepository.findByListing(listing);
        }
        return inspectionMapper.toInspectionResponse(inspection);
    }


    public List<InspectionResponse> getAll() {
        return inspectionRepository.findAll().stream()
                .map(inspectionMapper::toInspectionResponse)
                .toList();
    }

    public List<InspectionResponse> getMyAssign() {
        User  user = currentUserService.getCurrentUser();

        List<Inspection> inspections = inspectionRepository.findByInspectorIdOrderByCreatedAt(user.getId());

        return inspections.stream()
                .map(inspectionMapper::toInspectionResponse)
                .toList();
    }

    public InspectionResponse assignInspector(InspectionAssignRequest inspectionAssignRequest) {
        Inspection inspection = inspectionRepository.findById(inspectionAssignRequest.getInspectionId())
                .orElseThrow(()-> new AppException(ErrorCode.INSPECTION_NOT_FOUND));
        inspection.setStatus(InspectionStatus.PENDING_ASSIGNED);

        User inspector = userRepository.findById(inspectionAssignRequest.getInspectorId())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        inspection.setInspector(inspector);
        inspection.setStatus(InspectionStatus.IN_PROGRESS);
        return inspectionMapper.toInspectionResponse(inspectionRepository.save(inspection));
    }

    public List<InspectionResponse> getPending() {
        return inspectionRepository.findByStatusOrderByCreatedAt(InspectionStatus.PENDING_ASSIGNED)
                .stream()
                .map(inspectionMapper::toInspectionResponse)
                .toList();
    }



    public List<UserResponse> getAvailableInspector(Date scheduleAt) {
        Date expiryAt = Date.from(scheduleAt.toInstant().plus(2, ChronoUnit.HOURS));
        return userRepository.findAvailableInspectors(scheduleAt, expiryAt).stream()
                .map(userMapper::toUserResponse)
                .toList();
    }


    public InspectionResponse getInspectionById(UUID id) {
        Inspection inspection = inspectionRepository.findById(id).orElse(null);
        return inspectionMapper.toInspectionResponse(inspection);
    }

    public List<InspectionResponse> getMyInspection() {
        return inspectionRepository.findAll().stream()
                .map(inspectionMapper::toInspectionResponse)
                .toList();
    }

    public PageResponse<InspectionResponse> getPageInspection(int page, int size) {
        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page, size, sort);

        var pageData = inspectionRepository.findAll(pageable);

        return PageResponse.<InspectionResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .data(pageData.getContent().stream()
                        .map(inspectionMapper::toInspectionResponse)
                        .toList())
                .build();
    }
}
