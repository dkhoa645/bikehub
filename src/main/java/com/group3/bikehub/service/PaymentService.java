package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.ApiResponse;
import com.group3.bikehub.dto.request.OrderCreationRequest;
import com.group3.bikehub.dto.request.PaymentCreationRequest;
import com.group3.bikehub.dto.request.PaymentFilterRequest;
import com.group3.bikehub.dto.response.PageResponse;
import com.group3.bikehub.dto.response.PaymentCreationResponse;
import com.group3.bikehub.dto.response.PaymentResponse;
import com.group3.bikehub.entity.*;
import com.group3.bikehub.entity.Enum.*;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.mapper.PaymentMapper;
import com.group3.bikehub.repository.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
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
    OrderLocationRepository orderLocationRepository;
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

        if(buyer.getAddress() == null){
            throw new AppException(ErrorCode.ADDRESS_NOT_REGISTERED);
        }

        //Chống spam 5 lần
        int spam = orderRepository.countExpiredOrdersByUser(buyer.getId(), OrderStatus.EXPIRED, SellerStatus.PENDING);
        if(spam>= 5) throw new AppException(ErrorCode.ORDER_SPAMMING);

        //Check listing được đặt chưa và chuyển trạng thái sang RESERVE

        int update = listingRepository.resolveListing(orderCreationRequest.getListingId(), ListingStatus.LIVE);
        if(update==0){
            throw new AppException(ErrorCode.LISTING_RESERVE);
        }

        CreatePaymentLinkResponse paymentLink = generatePaymentLink(2000L, "Dat coc don hang");

        OrderLocation orderLocation = orderLocationRepository.save(OrderLocation.builder()
                .addressLine(buyer.getAddress().getAddressLine())
                .nameContact(buyer.getAddress().getNameContact())
                .phoneContact(buyer.getAddress().getPhoneContact())
                .build());

        Order order = orderRepository.save(Order.builder()
                .buyer(buyer)
                .seller(listing.getSeller())
                .listing(listing)
                .totalAmount(listing.getPrice())
                .depositAmount(listing.getPrice().multiply(new BigDecimal("0.10")))
                .createdAt(new Date())
                .expiresAt(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .sellerStatus(SellerStatus.PENDING)
                        .orderLocation(orderLocation)
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
        order.setExpiresAt(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()));

        OrderLog orderLog = OrderLog.builder()
                .order(order)
                .status(OrderStatus.PAID)
                .createdAt(Date.from(Instant.now()))
                .build();
        order.getLogs().add(orderLog);
        orderRepository.save(order);
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
                .returnUrl("http://localhost:5173")
                .cancelUrl("http://localhost:5173")
                .build();

        return payOS.paymentRequests().create(paymentRequest);
    }




    public PaymentCreationResponse createSubscriptionPayment(PaymentCreationRequest paymentCreationRequest) {
        Subscription subscription = subscriptionRepository.findById(paymentCreationRequest.getSubscriptionId())
                .orElseThrow(()-> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));

        User user = currentUserService.getCurrentUser();

        Listing listing = subscription.getListing();

        if(!listing.getStatus().equals(ListingStatus.DRAFT)){
            throw new AppException(ErrorCode.LISTING_STATUS);
        }

        listing.getSubscriptions().forEach(sub -> {
            if(sub.getStatus().equals(SubscriptionStatusEnum.ACTIVE)){
                throw new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND);
            }
        });

        CreatePaymentLinkResponse paymentLink = generatePaymentLink(subscription.getPlan().getPrice().divide(BigDecimal.valueOf(1000L)).longValue() , "Tien mua goi");

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
        List<Payment> list = paymentRepository.findByUserOrderByCreateAtDesc(user);
        return list.stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();

    }

    public void createPayoutBatch(String batchReferenceId, Address address, String description) {
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
        payOs1.payouts().batch().create(payoutRequest);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void expireOrders() {
        List<Order> orders = getExpiredOrders();
        for(Order order : orders){
            try {
                handleExpiredOrders(order);
            } catch (Exception e) {
                log.error("Expire order failed for order {}", order.getId(), e);
            }
        }
    }

    private List<Order> getExpiredOrders(){
        return orderRepository.findByOrderStatusInAndExpiresAtBefore(
                List.of(OrderStatus.PENDING, OrderStatus.PAID, OrderStatus.DELIVERED),
                new Date());
    }

    public void handleExpiredOrders(Order order){
            if(order.getOrderStatus().equals(OrderStatus.PAID)) {
                order.setSellerStatus(SellerStatus.REJECTED);
                order.setOrderStatus(OrderStatus.CONFIRMED);
                order.setExpiresAt(null);
                setListingToLive(order);
            }else if (order.getOrderStatus().equals(OrderStatus.PENDING)) {
                order.setOrderStatus(OrderStatus.EXPIRED);
                order.setExpiresAt(null);
                setListingToLive(order);
            }else if(order.getOrderStatus().equals(OrderStatus.DELIVERED)){
                order.setOrderStatus(OrderStatus.COMPLETE);
                order.setExpiresAt(null);
                order.getListing().setStatus(ListingStatus.SOLD);
                OrderLog orderLog = OrderLog.builder()
                        .order(order)
                        .status(OrderStatus.COMPLETE)
                        .createdAt(Date.from(Instant.now()))
                        .build();
                order.getLogs().add(orderLog);
                orderRepository.save(order);
            }
    }

    private void setListingToLive(Order order) {
        Listing listing = order.getListing();
        listing.setStatus(ListingStatus.LIVE);
        listingRepository.save(listing);
    }


//    @Scheduled(fixedRate = 60000)
//    @Transactional
//    public void refundOrders() {
//        List<Order> orders = getRefundableOrders();
//        for (Order order : orders) {
//            try {
//                refundSingleOrder(order);
//            }catch(Exception e){
//                log.error("Refund failed for order ", e);
//                throw new AppException(ErrorCode.PAYOUT);
//            }
//        }
//    }


    private void refundSingleOrder(Order order) {
        if (order.getOrderStatus() == OrderStatus.REFUND) {
            return;
        }
            String referenceId = PaymentType.REFUND + "_" + order.getId();
            createPayoutBatch(referenceId,
                    order.getBuyer().getAddress(),
                    "Hoan tien cho Buyer");
            order.setOrderStatus(OrderStatus.REFUND);
            order.getListing().setStatus(ListingStatus.LIVE);
            paymentRepository.save(Payment.builder()
                    .user(order.getBuyer())
                    .status(PaymentStatus.SUCCESS)
                    .amount(order.getDepositAmount())
                    .referenceType(ReferenceType.ORDER)
                    .referenceId(referenceId)
                            .createAt(Date.from(Instant.now()))
                            .paidAt(new Date())
                    .type(PaymentType.REFUND)
                    .build());
    }

    private List<Order> getRefundableOrders(){
        return orderRepository.findByOrderStatusAndSellerStatus(
                OrderStatus.CONFIRMED, SellerStatus.REJECTED
        );
    }


//    @Scheduled(fixedRate = 60000)
//    @Transactional
//    public void completeOrders() {
//        List<Order> orders = getCompleteOrders();
//        for (Order order : orders) {
//            try {
//                completeOrder(order);
//            }catch(Exception e){
//                log.error("Complete failed for order ", e);
//                throw new AppException(ErrorCode.PAYOUT);
//            }
//        }
//    }


    private void completeOrder(Order order) {
        order.setSellerStatus(SellerStatus.PAID);
        String referenceId = PaymentType.PAYOUT + "_" + order.getId();
        createPayoutBatch(referenceId,
                order.getSeller().getAddress(),
                "Thanh toan tien coc");
        Payment payment = Payment.builder()
                .type(PaymentType.PAYOUT)
                .status(PaymentStatus.SUCCESS)
                .referenceId(referenceId)
                .amount(order.getDepositAmount())
                .paidAt(Date.from(Instant.now()))
                .createAt(Date.from(Instant.now()))
                .referenceType(ReferenceType.ORDER)
                .referenceId(String.valueOf(order.getId()))
                .user(order.getSeller())
                .build();
        paymentRepository.save(payment);
    }

    private List<Order> getCompleteOrders() {
        return orderRepository.
                findByOrderStatusAndSellerStatus(OrderStatus.COMPLETE, SellerStatus.ACCEPTED);
    }


    public PageResponse<PaymentResponse> getPagePayment(PaymentFilterRequest request) {
        Sort sort = Sort.by(
                Sort.Order.desc("createAt")
        );

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()-1, sort);

        var pageDate = paymentRepository.findAll(pageable, request.getStatus(), request.getStartDate(), request.getEndDate());

        BigDecimal intermediary = paymentRepository.sumIntermediary(request.getStartDate()
                , request.getEndDate(), request.getStatus());
        BigDecimal sumRefund = paymentRepository.sumRefund(request.getStartDate()
                , request.getEndDate(), request.getStatus());
        BigDecimal subscription =  paymentRepository.sumSubscription(request.getStartDate()
                , request.getEndDate(), request.getStatus());

        Map<String,Object> metadata = Map.of(
                "intermediary", intermediary,
                "refund", sumRefund,
                "subscription", subscription
        );

        return PageResponse.<PaymentResponse>builder()
                .currentPage(request.getPage())
                .totalElements(pageDate.getTotalElements())
                .totalPage(pageDate.getTotalPages())
                .pageSize(pageDate.getSize())
                .data(pageDate.getContent().stream()
                        .map(paymentMapper::toPaymentResponse)
                        .toList())
                .meta(metadata)
                .build();
    }
}
