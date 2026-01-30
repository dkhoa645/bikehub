package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.KycResponse;
import com.group3.bikehub.entity.Kyc;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KycMapper {
    KycResponse toKycResponse(Kyc kyc);
    Kyc toKyc(KycResponse kycResponse);


}
