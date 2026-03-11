package com.group3.bikehub.repository;

import com.group3.bikehub.dto.response.TransactionResponse;
import com.group3.bikehub.entity.Transaction;
import com.group3.bikehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findTransactionByFromUserOrToUser(User fromUser, User toUser);
}
