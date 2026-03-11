package com.group3.bikehub.mapper;

import com.group3.bikehub.dto.response.TransactionResponse;
import com.group3.bikehub.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toResponse(Transaction transaction);
}
