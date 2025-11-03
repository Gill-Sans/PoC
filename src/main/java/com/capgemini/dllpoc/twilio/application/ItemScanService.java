package com.capgemini.dllpoc.twilio.application;

import com.capgemini.dllpoc.twilio.ports.in.ItemScanUseCase;
import org.springframework.stereotype.Service;

@Service
public class ItemScanService implements ItemScanUseCase {
    public String handleItemScanIssue(String description) {
        return "Your request has been sent to service central";
    }
}
