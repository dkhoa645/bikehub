package com.group3.bikehub.controller;


import com.group3.bikehub.configuration.PayOSConfig;
import com.group3.bikehub.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.PayOS;


import java.util.Map;


@Slf4j

@RestController
public class WebHookController {
    @Autowired
    PayOS payOS;
    @Autowired
    PaymentService paymentService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> body) {

        try {

            String signature = (String) body.get("signature");
            Map<String, Object> data = (Map<String, Object>) body.get("data");

            if (!paymentService.isValidData(data, signature)) {
                return ResponseEntity.badRequest().body("Invalid signature");
            }

            System.out.println("Thanh toán thành công orderCode: "
                    + data.get("orderCode"));
            log.info("data : {}", body);
            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }
}
