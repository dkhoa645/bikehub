package com.group3.bikehub.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;

@Configuration
    public class PayOSConfig {

        @Value("${com.payos.PAYOS_CLIENT_ID}")
        private String clientId;

        @Value("${com.payos.PAYOS_API_KEY}")
        private String apiKey;

        @Value("${com.payos.PAYOS_CHECKSUM_KEY}")
        private String checksumKey;

        @Bean
        public PayOS payOS() {
            return new PayOS(clientId, apiKey, checksumKey);
        }
    }

