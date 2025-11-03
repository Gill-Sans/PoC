package com.capgemini.dllpoc.ai.delhaize.application;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CallFlowAgent {

    private final AgentService agentService;

    public CallFlowAgent(AgentService agentService) {
        this.agentService = agentService;
    }

    public String process(Map<String, String> params) {
        String input = "Hi";
        return agentService.chat(input);
    }

    public String talk(String input) {
        return agentService.chat(input);
    }
}
