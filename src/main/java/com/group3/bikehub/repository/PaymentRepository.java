package com.group3.bikehub.repository;

import com.group3.bikehub.dto.request.PaymentFilterRequest;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.PaymentType;
import com.group3.bikehub.entity.Enum.ReferenceType;
import com.group3.bikehub.entity.Payment;
import com.group3.bikehub.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPayosOrderCode(Long payosOrderCode);

    List<Payment> findByUserOrderByPaidAt(User user);

    Optional<Payment> findByReferenceId(String referenceId);

    @Query("""
        SELECT p
        FROM Payment p
        WHERE (:status IS NULL OR p.status = :status)
        AND (:startDate IS NULL OR p.paidAt >= :startDate)
        AND (:endDate IS NULL OR p.paidAt <= :endDate)
        """)
    Page<Payment> findAll(Pageable pageable,
                          @Param("status")PaymentStatus status,
                          @Param("startDate")Date startDate,
                          @Param("endDate")Date endDate);


    // 1. Tiền trung gian
    @Query("""
        SELECT COALESCE(
                   SUM(CASE WHEN p.type = 'PAYMENT'
                            AND p.referenceType = 'ORDER'
                            AND p.status = 'SUCCESS'
                       THEN p.amount ELSE 0 END),0)
             - COALESCE(SUM(CASE WHEN p.type = 'REFUND' THEN p.amount ELSE 0 END),0)
             - COALESCE(SUM(CASE WHEN p.type = 'PAYOUT' THEN p.amount ELSE 0 END),0)
        FROM Payment p
        WHERE (:startDate IS NULL OR p.paidAt >= :startDate)
          AND (:endDate IS NULL OR p.paidAt <= :endDate)
          AND (:status IS NULL OR p.status = :status)
    """)
    BigDecimal sumIntermediary(@Param("startDate") Date startDate,
                               @Param("endDate") Date endDate,
                               @Param("status") PaymentStatus status);

    // 2. Tiền refund
    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.type = 'REFUND'
          AND (:startDate IS NULL OR p.paidAt >= :startDate)
          AND (:endDate IS NULL OR p.paidAt <= :endDate)
          AND (:status IS NULL OR p.status = :status)
    """)
    BigDecimal sumRefund(@Param("startDate") Date startDate,
                         @Param("endDate") Date endDate,
                         @Param("status") PaymentStatus status);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0)
        FROM Payment p
        WHERE p.type = 'PAYMENT'
          AND p.referenceType = 'SUBSCRIPTION'
          AND (:startDate IS NULL OR p.paidAt >= :startDate)
          AND (:endDate IS NULL OR p.paidAt <= :endDate)
          AND (:status IS NULL OR p.status = :status)
    """)
    BigDecimal sumSubscription(@Param("startDate") Date startDate,
                               @Param("endDate") Date endDate,
                               @Param("status") PaymentStatus status);
}
