package com.capgemini.dllpoc.ai.delhaize.application;

import com.twilio.twiml.voice.Say;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final Map<String, String> phoneToSessionMap = new ConcurrentHashMap<>();
    private final Map<String, SessionData> sessionDataMap = new ConcurrentHashMap<>();

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
        String sessionId = phoneToSessionMap.get(callSid);
        sessionDataMap.remove(sessionId);
    }

    /**
     * Create a new session and return its ID
     */
    public String createNewSession() {
        return UUID.randomUUID().toString();
    }

    public SessionData getSessionData(String sessionId) {
        return sessionDataMap.computeIfAbsent(sessionId, k -> new SessionData());
    }

    public void setLanguage(String sessionId, Say.Language language) {
        getSessionData(sessionId).setLanguage(language);
    }

    public Say.Language getLanguage(String sessionId) {
        SessionData sessionData = sessionDataMap.get(sessionId);
        return sessionData != null ? sessionData.getLanguage() : null;
    }

    public void setStoreNumber(String sessionId, String storeNumber) {
        getSessionData(sessionId).setStoreNumber(storeNumber);
    }

    public String getStoreNumber(String sessionId) {
        SessionData sessionData = sessionDataMap.get(sessionId);
        return sessionData != null ? sessionData.getStoreNumber() : null;
    }
}