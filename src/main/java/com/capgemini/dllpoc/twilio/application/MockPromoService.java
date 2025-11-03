package com.capgemini.dllpoc.twilio.application;

import com.capgemini.dllpoc.twilio.ports.in.MockPromoServiceUseCase;
import org.springframework.stereotype.Service;

@Service
public class MockPromoService implements MockPromoServiceUseCase {
    public String handlePromoUpdate(String description) {
        return "Your promotion update request has been handled";
    }
}
