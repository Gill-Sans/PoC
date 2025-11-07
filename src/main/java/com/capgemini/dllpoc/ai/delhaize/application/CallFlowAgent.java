package com.capgemini.dllpoc.ai.delhaize.application;

import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CallFlowAgent {

    private final AgentService conversationAgent;
    private final SessionService sessionService;
    private final TwilioResponseBuilder twilioResponseBuilder;

    public String process(Map<String, String> params) {
        // Extract call SID or use a default identifier for session management
        String callSid = params.getOrDefault("CallSid", "default-call");
        String sessionId = sessionService.getOrCreateSessionId(callSid);

        String input = extractUserInput(params, sessionId);
        return conversationAgent.chat(sessionId, input);
    }

    public String talk(String input) {
        // For direct talk endpoint, we could use a different session strategy
        // For now, use a generic session ID or extract from request context
        String sessionId = sessionService.createNewSession();
        return conversationAgent.chat(sessionId, input);
    }

    public String talkWithSession(String sessionId, String input) {
        return conversationAgent.chat(sessionId, input);
    }

    public void clearConversation(String callSid) {
        String sessionId = sessionService.getSessionId(callSid);
        if (sessionId != null) {
            conversationAgent.clearConversation(sessionId);
            sessionService.clearSession(callSid);
        }
    }

    private String extractUserInput(Map<String, String> params, String sessionId) {
        if(params.containsKey("Digits")) {
            String digits = params.get("Digits");
            Say.Language currentLanguage = sessionService.getLanguage(sessionId);
            if (currentLanguage == null) {
                try {
                    int input = Integer.parseInt(digits);
                    Map<Integer, Say.Language> languageMap = twilioResponseBuilder.getLanguageDigitMapping();

                    if (languageMap.containsKey(input)) {
                        Say.Language language = languageMap.get(input);
                        sessionService.setLanguage(sessionId, language);
                    }
                    return "Selected language: " + languageMap.get(input).toString();
                } catch (NumberFormatException e) {
                    return "Invalid input for language selection.";
                }
            }
            return "User entered: " + digits;
        }

        if(params.containsKey("SpeechResult")) {
            return params.get("SpeechResult");
        }

        return "Hi";
    }
}
