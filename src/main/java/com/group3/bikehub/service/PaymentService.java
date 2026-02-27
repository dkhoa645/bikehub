package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.CreateListingPaymentRequest;
import com.group3.bikehub.dto.request.CreateOrderPaymentRequest;
import com.group3.bikehub.entity.Enum.OrderStatus;
import com.group3.bikehub.entity.Enum.PaymentStatus;
import com.group3.bikehub.entity.Enum.PaymentType;
import com.group3.bikehub.entity.Listing;
import com.group3.bikehub.entity.Order;
import com.group3.bikehub.entity.Payment;
import com.group3.bikehub.entity.User;
import com.group3.bikehub.exception.AppException;
import com.group3.bikehub.exception.ErrorCode;
import com.group3.bikehub.repository.ListingRepository;
import com.group3.bikehub.repository.OrderRepository;
import com.group3.bikehub.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class PaymentService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CurrentUserService currentUserService;
    @Autowired
    ListingService listingService;
    @Autowired
    OrderService orderService;
    @Autowired
    PaymentRepository paymentRepository;
    private final Random random = new Random();
    @Autowired
    ListingRepository listingRepository;
    @Autowired
    PayOS payOS;
    @Value("${com.payos.PAYOS_CHECKSUM_KEY}")
    private String CHECKSUM_KEY;

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
                .amount(order.getTotal_ammount().longValue())
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
    public Map<String, Object> createListingPayment(CreateListingPaymentRequest request) {
        Optional<Listing> listing = listingRepository.findById(request.getListing_id());
        User seller = currentUserService.getCurrentUser();
        if (!seller.getId().equals(listing.get().getSeller().getId())) {
            throw new AppException(ErrorCode.INVALID_SELLER_ID);
        }
        String randomSuffix = String.format("%04d", random.nextInt(10000));
        long orderCode = Long.parseLong(20 + randomSuffix);
        CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(10000L)
                .description(" ")
                .returnUrl("test")
                .cancelUrl("test")
                .build();
        CreatePaymentLinkResponse paymentResult = payOS.paymentRequests().create(paymentData);
        Payment payment = new Payment();
        payment.setReferenceId(String.valueOf(listing.get().getId()));
        payment.setType(PaymentType.LISTING);
        payment.setPayosOrderCode(orderCode);
        paymentRepository.save(payment);
        return Map.of(
                "message", "Tạo đơn hàng thành công. Vui lòng thanh toán .",
                "ListingID", listing.get().getId(),
                "paymentUrl", paymentResult.getCheckoutUrl()
        );

    }
    public void handleWebHook(String orderCode){
        Long payosOrderId = Long.parseLong(orderCode);
        Payment payment = paymentRepository.findByPayosOrderCode(payosOrderId);
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
        if (payment.getType().equals(PaymentType.ORDER)) {
            orderService.handleOrderPayment(payment);
        }else if (payment.getType().equals(PaymentType.LISTING)) {
            listingService.handleListingPayment(payment);
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

}
