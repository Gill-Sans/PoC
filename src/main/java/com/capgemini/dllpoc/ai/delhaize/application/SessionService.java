package com.capgemini.dllpoc.ai.delhaize.application;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final Map<String, String> phoneToSessionMap = new ConcurrentHashMap<>();

    /**
     * Get or create a session ID for a given phone number/caller identifier
     */
    public String getOrCreateSessionId(String callSid) {
        return phoneToSessionMap.computeIfAbsent(callSid, k -> UUID.randomUUID().toString());
    }

    /**
     * Get existing session ID for a phone number, or null if not found
     */
    public String getSessionId(String callSid) {
        return phoneToSessionMap.get(callSid);
    }

    /**
     * Clear session for a phone number
     */
    public void clearSession(String callSid) {
        phoneToSessionMap.remove(callSid);
    }

    /**
     * Create a new session and return its ID
     */
    public String createNewSession() {
        return UUID.randomUUID().toString();
    }
}