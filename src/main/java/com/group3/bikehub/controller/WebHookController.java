package com.group3.bikehub.controller;


import com.group3.bikehub.configuration.PayOSConfig;
import com.group3.bikehub.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.PayOS;


import java.util.Map;
import vn.payos.model.webhooks.WebhookData;


@Slf4j

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebHookController {

    PayOS payOS;

    PaymentService paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> body) {

        try {

            WebhookData verifiedData = payOS.webhooks().verify(body);

            if (verifiedData != null) {
                String orderCode = String.valueOf(verifiedData.getOrderCode());
                paymentService.handleWebHook(orderCode);
                log.info("Thanh toán thành công orderCode: {}", orderCode);
            } else {
                log.warn("Webhook verify failed or test call");
            }

        } catch (Exception e) {
            log.error("Webhook error", e);
        }

        return ResponseEntity.ok("OK");
    }
}

