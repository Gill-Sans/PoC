package com.capgemini.dllpoc.ai.delhaize.application;

import com.capgemini.dllpoc.ai.delhaize.tools.*;
import com.capgemini.dllpoc.config.properties.LanguageProperties;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final List<Object> tools;

    public AgentService(ChatClient chatClient, ChatMemory chatMemory, TwilioResponseBuilder twilioResponseBuilder, LanguageProperties languageProperties) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
        this.tools = createTools(twilioResponseBuilder, languageProperties);
    }

    private List<Object> createTools(TwilioResponseBuilder twilioResponseBuilder, LanguageProperties languageProperties) {
        return List.of(
                new AskLanguageTool(twilioResponseBuilder),
                new AskStoreNumberTool(twilioResponseBuilder, languageProperties),
                new AskStoreNameTool(twilioResponseBuilder, languageProperties),
                new AskProblemTool(twilioResponseBuilder, languageProperties),
                new ConfirmProblemTool(twilioResponseBuilder, languageProperties),
                new TranscriptionTool(),
                new HangupTool(twilioResponseBuilder, languageProperties)
        );
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
            
            DO NOT add quotes to the XML but format it with appropriate indentation.
            DO NOT change the answer from the tool.
            
            Do not alter the response content in any way. Preserve the exact structure and data as returned by the tool.
            Do not enclose the response in quotes. Ensure the output is returned as raw XML without surrounding quotation marks.
            Format the XML with appropriate indentation to improve readability, while maintaining the original semantics and structure.

            Follow this conversation flow:
            1. First interaction: call askLanguage() to let user select language
            2. After language selected: call askStoreName(language) to get store name
            3. After name provided: call askStoreNumber(language) to get store number
            4. After store number provided: call askProblem(language) to ask the customer to describe their problem
            5. After collecting all information, call confirmProblem(language) to confirm the details with the customer
            6. After collecting all information, call hangupTool(language) to finalize the call
            

            Always use the conversation history to understand the current state.
            If a customer has already provided information, don't ask for it again.
            Use the appropriate language based on what the customer selected.
            """;
    }

    public void clearConversation(String sessionId) {
        chatMemory.clear(sessionId);
    }
}