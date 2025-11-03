package com.capgemini.dllpoc.twilio.application;

import com.capgemini.dllpoc.twilio.model.CallData;
import com.capgemini.dllpoc.twilio.ports.in.CallDataUseCase;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CallDataService implements CallDataUseCase {
    private final Map<String, CallData> callDataMap = new ConcurrentHashMap<>();

    public CallData getOrCreate(String callSid, String lang) {
        return callDataMap.computeIfAbsent(callSid, k -> new CallData(lang));
    }

    public CallData get(String callSid) {
        return callDataMap.get(callSid);
    }

    public void update(String callSid, CallData data) {
        callDataMap.put(callSid, data);
    }

    public Map<String, CallData> getAll() {
        return callDataMap;
    }
}
