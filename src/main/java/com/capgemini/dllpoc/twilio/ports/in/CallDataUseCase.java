package com.capgemini.dllpoc.twilio.ports.in;

import com.capgemini.dllpoc.twilio.model.CallData;

import java.util.Map;

public interface CallDataUseCase {
    CallData getOrCreate(String callSid, String digits);
    CallData get(String callSid);
    void update(String callSid, CallData data);
    Map<String, CallData> getAll();
}
