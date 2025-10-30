package com.capgemini.dllpoc.service;

import org.springframework.stereotype.Service;

@Service
public class ItemScanService {
    public String handleItemScanIssue(String description) {
        return "Your request has been sent to service central";
    }
}
