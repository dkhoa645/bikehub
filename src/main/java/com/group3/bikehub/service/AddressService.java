package com.group3.bikehub.service;

import com.group3.bikehub.dto.request.AddressCreationRequest;
import com.group3.bikehub.dto.request.AddressUpdateRequest;
import com.group3.bikehub.dto.response.AddressResponse;
import com.group3.bikehub.entity.Address;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.AddressMapper;
import com.group3.bikehub.mapper.UserMapper;
import com.group3.bikehub.repository.AddressRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressRepository addressRepository;
    AddressMapper  addressMapper;
    CurrentUserService currentUserService;


    public AddressResponse create(AddressCreationRequest request) {
        Address address = addressMapper.toAddress(request);
        User user = currentUserService.getCurrentUser();
        if(user.getAddress()!=null){
            address.setUser(user);
        }else{
            throw new AppException(ErrorCode.ADDRESS_EXIST);
        }
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    public AddressResponse findMyAddress() {
        User user = currentUserService.getCurrentUser();
        return addressMapper.toAddressResponse(user.getAddress());
    }


    public AddressResponse updateMyAddress(AddressUpdateRequest request, UUID id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.LOCATION_NOT_FOUND)
        );
        addressMapper.updateAddress(request,address);
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }
}
