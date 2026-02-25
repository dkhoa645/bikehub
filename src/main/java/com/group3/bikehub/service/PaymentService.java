package com.group3.bikehub.service;


import com.group3.bikehub.dto.request.CreatePaymentRequest;
import com.group3.bikehub.entity.Order;
import com.group3.bikehub.entity.Payment;
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
    PaymentRepository paymentRepository;
    private final Random random = new Random();
    @Autowired
    PayOS payOS;
    @Value("${com.payos.PAYOS_CHECKSUM_KEY}")
    private String CHECKSUM_KEY;

    public Map<String, Object> create(CreatePaymentRequest request) {
        Order order = orderRepository.findOrderById(request.getOrder_id());
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
        payment.setOrder(order);
        payment.setPayosOrderCode(orderCode);
        paymentRepository.save(payment);
        return Map.of(
                "message", "Tạo đơn hàng thành công. Vui lòng thanh toán .",
                "OrderID", order.getId(),
                "paymentUrl", paymentResult.getCheckoutUrl()
        );

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
