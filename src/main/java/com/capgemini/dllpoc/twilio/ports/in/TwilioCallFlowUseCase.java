package com.capgemini.dllpoc.twilio.ports.in;

import com.capgemini.dllpoc.twilio.model.CallData;
import com.twilio.twiml.VoiceResponse;

public interface TwilioCallFlowUseCase {
    String askLanguageSelection(CallData data);
    VoiceResponse handleLanguageSelection(String callSid, String digits);
    String askAccount(String lang);
    String askName(String lang);
    String askProblem(String lang);
    String confirmDetails(String lang, CallData data);
    String handleConfirmation(String lang, String confirmation, CallData data);
}
