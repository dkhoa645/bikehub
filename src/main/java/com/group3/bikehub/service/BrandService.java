package com.group3.bikehub.service;

import com.cloudinary.api.exceptions.ApiException;
import com.group3.bikehub.dto.request.BrandCreationRequest;
import com.group3.bikehub.dto.request.BrandUpdateRequest;
import com.group3.bikehub.dto.response.BrandResponse;
import com.group3.bikehub.entity.Brand;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.BrandMapper;
import com.group3.bikehub.repository.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {
    BrandRepository brandRepository;
    BrandMapper brandMapper;

    public List<BrandResponse> getAllBrand() {
        return brandRepository.findAll()
                .stream()
                .map(brandMapper::toBrandResponse)
                .toList();
    }

    public BrandResponse addBrand(BrandCreationRequest request) {
        return brandMapper.toBrandResponse(
                brandRepository.save(brandMapper.toBrand(request)));
    }

    public BrandResponse updateBrand(BrandUpdateRequest request) {
        Brand brand = brandRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.VERIFICATION_TOKEN_EXPIRED));
        brand.setName(request.getName());
        return brandMapper.toBrandResponse(brandRepository.save(brand));
    }
}
