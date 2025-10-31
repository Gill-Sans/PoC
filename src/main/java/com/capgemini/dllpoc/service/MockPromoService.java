package com.capgemini.dllpoc.service;

import org.springframework.stereotype.Service;

@Service
public class MockPromoService {
    public String handlePromoUpdate(String description) {
        return "Your promotion update request has been handled";
    }
}
