package com.capgemini.dllpoc.service;

import com.capgemini.dllpoc.model.CallData;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CallDataService {
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
