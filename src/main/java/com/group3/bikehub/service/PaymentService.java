package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.OrderCreationRequest;
import com.group3.bikehub.dto.request.PaymentCreationRequest;
import com.group3.bikehub.dto.response.PaymentCreationResponse;
import com.group3.bikehub.dto.response.PaymentResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.*;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.PaymentMapper;
import com.group3.bikehub.repository.ListingRepository;
import com.group3.bikehub.repository.OrderRepository;
import com.group3.bikehub.repository.PaymentRepository;
import com.group3.bikehub.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v1.payouts.Payout;
import vn.payos.model.v1.payouts.batch.PayoutBatchItem;
import vn.payos.model.v1.payouts.batch.PayoutBatchRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {

    OrderRepository orderRepository;
    CurrentUserService currentUserService;
    PaymentRepository paymentRepository;
    Random random = new Random();
    SubscriptionRepository subscriptionRepository;
    ListingRepository listingRepository;
    PayOS payOS;

    @Value("${com.payos.PAYOS_CHECKSUM_KEY}")
    @NonFinal
    String CHECKSUM_KEY;
    @Value("${com.payout.PAYOUT_CLIENT_ID}")
    @NonFinal
    String PAYOUT_CLIENT_ID;
    @Value("${com.payout.PAYOUT_API_KEY}")
    @NonFinal
    String PAYOUT_API_KEY;
    @Value("${com.payout.PAYOUT_CHECKSUM_KEY}")
    @NonFinal
    String PAYOUT_CHECKSUM_KEY;

    SubscriptionService subscriptionService;

    PaymentMapper paymentMapper;

    @Transactional
    public PaymentCreationResponse createOrderPayment(OrderCreationRequest orderCreationRequest) {

        Listing listing = listingRepository.findById(orderCreationRequest.getListingId())
                .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));

        User buyer = currentUserService.getCurrentUser();

        //Chống spam 5 lần
        int spam = orderRepository.countExpiredOrdersByUser(buyer.getId(), OrderStatus.EXPIRED);
        if(spam>= 5) throw new AppException(ErrorCode.ORDER_SPAMMING);

        //Check listing được đặt chưa và chuyển trạng thái sang RESERVE
        int update = listingRepository.resolveListing(orderCreationRequest.getListingId(), ListingStatus.LIVE);
        if(update==0){
            throw new AppException(ErrorCode.LISTING_RESERVE);
        }

        CreatePaymentLinkResponse paymentLink = generatePaymentLink(2000L, "Dat coc don hang");

        Order order = orderRepository.save(Order.builder()
                .buyer(buyer)
                .seller(listing.getSeller())
                .listing(listing)
                .totalAmount(listing.getPrice())
                .depositAmount(listing.getPrice().multiply(new BigDecimal("0.10")))
                .createdAt(new Date())
                .expiresAt(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .sellerStatus(SellerStatus.PENDING)
                .orderStatus(OrderStatus.PENDING)
                .build());

        Payment payment = Payment.builder()
                .referenceId(String.valueOf(order.getId()))
                .type(PaymentType.PAYMENT)
                .referenceType(ReferenceType.ORDER)
                .payosOrderCode(paymentLink.getOrderCode())
                .amount(listing.getPrice().multiply(new BigDecimal("0.10")))
                .status(PaymentStatus.PENDING)
                .user(buyer)
                .createAt(new Date())
                .build();

        paymentRepository.save(payment);

        return PaymentCreationResponse.builder()
                .paymentUrl(paymentLink.getCheckoutUrl())
                .build();
    }


    public void handleWebHook(String orderCode){
        Long payosOrderId = Long.parseLong(orderCode);
        Payment payment = paymentRepository.findByPayosOrderCode(payosOrderId);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(new Date());
        paymentRepository.save(payment);

        if (payment.getReferenceType().equals(ReferenceType.ORDER)) {
            handleOrderPayment(payment);
        }else if (payment.getReferenceType().equals(ReferenceType.SUBSCRIPTION)) {
            subscriptionService.handleSubscriptionPayment(payment);
        }

    }

    public void handleOrderPayment(Payment payment){
        UUID orderId = UUID.fromString(payment.getReferenceId());
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.setOrderStatus(OrderStatus.PAID);
        order.setExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
        orderRepository.save(order);
        payment.setStatus(PaymentStatus.SUCCESS);
    }

    private Long generateOrderCode(){
        String randomSuffix = String.format("%04d", random.nextInt(10000));
        return Long.parseLong(20 + randomSuffix);
    }

    public CreatePaymentLinkResponse generatePaymentLink(Long amount, String description) {
        long orderCode = generateOrderCode();
        CreatePaymentLinkRequest paymentRequest = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(amount)
                .description(description)
                .returnUrl("http://localhost:5173/payment/result")
                .cancelUrl("http://localhost:5173/payment/result")
                .build();

        return payOS.paymentRequests().create(paymentRequest);
    }



    public boolean isValidData(Map<String, Object> data, String receivedSignature) {
        try {

            // 1️⃣ Sort key alphabet
            List<String> keys = new ArrayList<>(data.keySet());
            Collections.sort(keys);

            // 2️⃣ Build chuỗi key=value&key2=value2...
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                Object valueObj = data.get(key);
                String value = valueObj == null ? "" : valueObj.toString();

                sb.append(key).append("=").append(value);

                if (i < keys.size() - 1) {
                    sb.append("&");
                }
            }
            String dataToSign = sb.toString();

            // 3️⃣ HMAC SHA256
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec =
                    new SecretKeySpec(CHECKSUM_KEY.getBytes(StandardCharsets.UTF_8),
                            "HmacSHA256");

            mac.init(secretKeySpec);

            byte[] hash = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));

            String generatedSignature = bytesToHex(hash);

            return generatedSignature.equals(receivedSignature);

        } catch (Exception e) {
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public PaymentCreationResponse createSubscriptionPayment(PaymentCreationRequest paymentCreationRequest) {
        Subscription subscription = subscriptionRepository.findById(paymentCreationRequest.getSubscriptionId())
                .orElseThrow(()-> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        User user = currentUserService.getCurrentUser();

        if(!subscription.getListing().getStatus().equals(ListingStatus.DRAFT)){
            throw new AppException(ErrorCode.LISTING_STATUS);
        }

        CreatePaymentLinkResponse paymentLink = generatePaymentLink(2000L , "Tien mua goi");

        Payment payment = new Payment();
        payment.setReferenceId(String.valueOf(subscription.getId()));
        payment.setType(PaymentType.PAYMENT);
        payment.setReferenceType(ReferenceType.SUBSCRIPTION);
        payment.setPayosOrderCode(paymentLink.getOrderCode());
        payment.setAmount(subscription.getPlan().getPrice());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setUser(user);
        payment.setCreateAt(new Date());
        paymentRepository.save(payment);


        return PaymentCreationResponse.builder()
                .paymentUrl(paymentLink.getCheckoutUrl())
                .build();

    }

    public List<PaymentResponse> getAll() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    public List<PaymentResponse> myPayment() {
        User user = currentUserService.getCurrentUser();
        List<Payment> list = paymentRepository.findByUserOrderByPaidAt(user);
        return list.stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();

    }

    public Payout createPayoutBatch(String batchReferenceId, Address address, String description) {
        PayOS payOs1 = new PayOS(PAYOUT_CLIENT_ID,PAYOUT_API_KEY,PAYOUT_CHECKSUM_KEY);
        PayoutBatchRequest payoutRequest = PayoutBatchRequest.builder()
                .referenceId(batchReferenceId)
                .validateDestination(false)
                .payout(PayoutBatchItem.builder()
                        .referenceId(batchReferenceId)
                        .amount(2000L)
                        .description(description)
                        .toBin(address.getBankCode().getCode())
                        .toAccountNumber(address.getAccountNumber())
                        .build())
                .build();
        return payOs1.payouts().batch().create(payoutRequest);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expireOrders() {
        List<Order> orders = orderRepository
                .findByOrderStatusInAndExpiresAtBefore(
                        List.of(OrderStatus.PENDING, OrderStatus.PAID),
                        new Date()
                );

        for (Order order : orders) {
            if(order.getOrderStatus().equals(OrderStatus.PAID)) {
                order.setSellerStatus(SellerStatus.REJECTED);
                order.setOrderStatus(OrderStatus.CONFIRMED);
            }else {
                order.setOrderStatus(OrderStatus.EXPIRED);
            }
            Listing listing = order.getListing();
            listing.setStatus(ListingStatus.LIVE);
            listingRepository.save(listing);
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void refundOrders() {
        List<Order> orders = orderRepository.findByOrderStatusAndSellerStatus(
                OrderStatus.CONFIRMED, SellerStatus.REJECTED
        );

        for (Order order : orders) {
            //Refund tien cho buyer
            if(order.getOrderStatus() == OrderStatus.REFUND){
                continue;
            }

            String referenceId = PaymentType.REFUND + "_" +order.getId();

            try {
                createPayoutBatch(
                        referenceId,
                        order.getBuyer().getAddress(),
                        "Hoan tien cho nguoi dung");
                order.setOrderStatus(OrderStatus.REFUND);
                order.getListing().setStatus(ListingStatus.LIVE);
                paymentRepository.save(Payment.builder()
                        .user(order.getBuyer())
                        .status(PaymentStatus.SUCCESS)
                        .amount(order.getDepositAmount())
                        .referenceType(ReferenceType.ORDER)
                        .referenceId(referenceId)
                        .type(PaymentType.REFUND)
                        .build());

            }catch(Exception e){
                log.error("Refund failed for order {}", e);
                throw new AppException(ErrorCode.PAYOUT);
            }
        }
    }



}
