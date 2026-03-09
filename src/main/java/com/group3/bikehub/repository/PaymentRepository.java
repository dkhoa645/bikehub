package com.group3.bikehub.repository;

import com.group3.bikehub.entity.Payment;
import com.group3.bikehub.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPayosOrderCode(Long payosOrderCode);

    List<Payment> findByUserOrderByPaidAt(User user);

}
