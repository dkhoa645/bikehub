package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.CreateOrderPaymentRequest;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {

    OrderRepository orderRepository;

    CurrentUserService currentUserService;

    OrderService orderService;

    PaymentRepository paymentRepository;

    Random random = new Random();

    SubscriptionRepository subscriptionRepository;

    PayOS payOS;

    @Value("${com.payos.PAYOS_CHECKSUM_KEY}")
    @NonFinal
    String CHECKSUM_KEY;

    SubscriptionService subscriptionService;

    PaymentMapper paymentMapper;

    public Map<String, Object> createOrderPayment(CreateOrderPaymentRequest request) {
        Order order = orderRepository.findOrderById(request.getOrder_id());
        User buyer = currentUserService.getCurrentUser();
        if (!buyer.getId().equals(order.getBuyer().getId())) {
            throw new AppException(ErrorCode.INVALID_SELLER_ID);
        }
        if (!order.getOrderStatus().equals(OrderStatus.PENDING) ) {
            throw new AppException(ErrorCode.ORDER_CANCELED);
        }
        String randomSuffix = String.format("%04d", random.nextInt(10000));
        long orderCode = Long.parseLong(order.getId() + randomSuffix);
        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(order.getTotal_amount().longValue())
                .description(request.getDescription())
                .returnUrl("test")
                .cancelUrl("test")
                .build();
        CreatePaymentLinkResponse paymentResult = payOS.paymentRequests().create(paymentData);
        Payment payment = new Payment();
        payment.setReferenceId(String.valueOf(order.getId()));
        payment.setType(PaymentType.ORDER);
        payment.setPayosOrderCode(orderCode);
        paymentRepository.save(payment);
        return Map.of(
                "message", "Tạo đơn hàng thành công. Vui lòng thanh toán .",
                "OrderID", order.getId(),
                "paymentUrl", paymentResult.getCheckoutUrl()
        );

    }

    public void handleWebHook(String orderCode){
        Long payosOrderId = Long.parseLong(orderCode);
        Payment payment = paymentRepository.findByPayosOrderCode(payosOrderId);
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        if (payment.getType().equals(PaymentType.ORDER)) {
            orderService.handleOrderPayment(payment);
        }else if (payment.getType().equals(PaymentType.SUBSCRIPTION)) {
            subscriptionService.handleSubscriptionPayment(payment);
        }

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

        if(!subscription.getListing().getStatus().equals(ListingStatus.DRAFT)){
            throw new AppException(ErrorCode.LISTING_STATUS);
        }

        String randomSuffix = String.format("%04d", random.nextInt(10000));
        long orderCode = Long.parseLong(20 + randomSuffix);

        Payment payment = new Payment();
        payment.setReferenceId(String.valueOf(subscription.getId()));
        payment.setType(PaymentType.SUBSCRIPTION);
        payment.setPayosOrderCode(orderCode);
        payment.setAmount(subscription.getPlan().getPrice());
        payment.setStatus(PaymentStatus.PENDING);
        payment = paymentRepository.save(payment);


        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(2000L)
                .description(" ")
                .returnUrl("test")
                .cancelUrl("test")
                .build();

        CreatePaymentLinkResponse paymentResult = payOS.paymentRequests().create(paymentData);


        return PaymentCreationResponse.builder()
                .paymentUrl(paymentResult.getCheckoutUrl())
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
}
