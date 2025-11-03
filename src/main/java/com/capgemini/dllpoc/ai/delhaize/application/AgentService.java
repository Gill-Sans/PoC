package com.capgemini.dllpoc.ai.delhaize.application;

import com.capgemini.dllpoc.ai.delhaize.tools.TwilioResponseTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    private final ChatClient chatClient;
    private final List<Object> tools;

    public AgentService(ChatClient chatClient, TwilioResponseBuilder twilioResponseBuilder) {
        this.chatClient = chatClient;
        this.tools = List.of(new TwilioResponseTool(twilioResponseBuilder));
    }

    public String chat(String input) {

        String systemPrompt = """
            You must call the appropriate tool and return ONLY its XML result.
            Do not add any text before or after the tool result.
            Do not explain what you did.
            Just return the raw XML from the tool.
            
            For language selection: call askLanguage()
            For name input: call askName(language)  
            For account input: call askAccount(language)
            """;

        String result = chatClient.prompt()
                .system(systemPrompt)
                .user(input)
                .tools(tools.toArray())
                .call()
                .content();

        return result;
    }
}