package com.group3.bikehub.service;

import com.group3.bikehub.dto.response.TransactionResponse;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.mapper.TransactionMapper;
import com.group3.bikehub.repository.TransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionService {
    TransactionRepository transactionRepository;
    TransactionMapper transactionMapper;
    CurrentUserService currentUserService;

    public List<TransactionResponse> getAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    public List<TransactionResponse> getMyTransactions() {
        User  user = currentUserService.getCurrentUser();
        return transactionRepository.findTransactionByFromUserOrToUser(user,user).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }
}
