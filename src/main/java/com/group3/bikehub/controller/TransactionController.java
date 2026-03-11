package com.group3.bikehub.controller;

import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.response.TransactionResponse;
import com.group3.bikehub.repository.TransactionRepository;
import com.group3.bikehub.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    TransactionService transactionService;

    @GetMapping
    ApiResponse<List<TransactionResponse>> getAllTransactions(){
        return ApiResponse.<List<TransactionResponse>>builder()
                .result(transactionService.getAll())
                .build();
    }

    @GetMapping("/my-transaction")
    ApiResponse<List<TransactionResponse>> getMyTransactions(){
        return ApiResponse.<List<TransactionResponse>>builder()
                .result(transactionService.getMyTransactions())
                .build();
    }
}
