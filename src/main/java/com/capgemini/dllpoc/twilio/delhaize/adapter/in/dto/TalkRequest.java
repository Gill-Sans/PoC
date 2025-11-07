package com.capgemini.dllpoc.twilio.delhaize.adapter.in.dto;

public record TalkRequest(String input, String sessionId) {
    
    // Constructor for backward compatibility
    public TalkRequest(String input) {
        this(input, null);
    }
}