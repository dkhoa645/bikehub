package com.group3.bikehub.service.impl;

import com.group3.bikehub.dto.response.KycResponse;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KycDraftStoreService {

        private final Map<String, KycResponse> store = new ConcurrentHashMap<>();

        public String save(KycResponse response) {
            String id = UUID.randomUUID().toString();
            store.put(id, response);
            return id;
        }
        public KycResponse get(String id) {
            return store.get(id);
    }


        public void remove(String id) {
            store.remove(id);
        }
    }


