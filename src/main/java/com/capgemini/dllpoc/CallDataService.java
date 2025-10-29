package com.capgemini.dllpoc;

import org.springframework.stereotype.Service;

@Service
public class CallDataService {
    public void handleCompletedCall(String callSid, TwilioController.CallData data){
        System.out.println("📞 [MockService] Persisting call: " + callSid + " -> " + data);
    }
}
