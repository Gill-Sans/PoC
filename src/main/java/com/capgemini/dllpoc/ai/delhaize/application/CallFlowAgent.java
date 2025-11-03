package com.capgemini.dllpoc.ai.delhaize.application;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CallFlowAgent {

    private final ConversationAgent conversationAgent;
    private final SessionService sessionService;

    public CallFlowAgent(ConversationAgent conversationAgent, SessionService sessionService) {
        this.conversationAgent = conversationAgent;
        this.sessionService = sessionService;
    }

    public String process(Map<String, String> params) {
        // Extract call SID or use a default identifier for session management
        String callSid = params.getOrDefault("CallSid", "default-call");
        String sessionId = sessionService.getOrCreateSessionId(callSid);
        
        String input = "Hi";
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
}
