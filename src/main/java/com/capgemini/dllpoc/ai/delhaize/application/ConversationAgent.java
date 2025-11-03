package com.capgemini.dllpoc.ai.delhaize.application;

import com.capgemini.dllpoc.ai.delhaize.tools.TwilioResponseTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationAgent {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final List<Object> tools;

    public ConversationAgent(ChatClient chatClient, ChatMemory chatMemory, TwilioResponseBuilder twilioResponseBuilder) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.tools = List.of(new TwilioResponseTool(twilioResponseBuilder));
    }

    public String chat(String sessionId, String input) {
        // Retrieve conversation history for this session
        List<Message> conversationHistory = chatMemory.get(sessionId);
        
        // Add new user message to memory
        UserMessage userMessage = new UserMessage(input);
        chatMemory.add(sessionId, userMessage);

        // Create system prompt for conversation flow
        String systemPrompt = buildSystemPrompt();

        // Build conversation context with history
        var callRequest = chatClient.prompt()
                .system(systemPrompt)
                .user(input)
                .tools(tools.toArray());

        // Add conversation history if available
        if (!conversationHistory.isEmpty()) {
            callRequest.messages(conversationHistory);
        }

        String result = callRequest.call().content();

        // Store assistant response in memory
        AssistantMessage assistantMessage = new AssistantMessage(result);
        chatMemory.add(sessionId, assistantMessage);

        return result;
    }

    private String buildSystemPrompt() {
        return """
            You are a helpful customer service assistant for a retail store.
            You must call the appropriate tool and return ONLY its XML result.
            Do not add any text before or after the tool result.
            Do not explain what you did.
            Just return the raw XML from the tool.
            
            Follow this conversation flow:
            1. First interaction: call askLanguage() to let user select language
            2. After language selected: call askName(language) to get customer name
            3. After name provided: call askAccount(language) to get account number
            4. Continue the conversation based on context and previous exchanges
            
            Always use the conversation history to understand the current state.
            If a customer has already provided information, don't ask for it again.
            Use the appropriate language based on what the customer selected.
            """;
    }

    public void clearConversation(String sessionId) {
        chatMemory.clear(sessionId);
    }

    public List<Message> getConversationHistory(String sessionId) {
        return chatMemory.get(sessionId);
    }
}