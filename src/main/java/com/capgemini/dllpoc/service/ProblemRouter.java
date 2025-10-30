package com.capgemini.dllpoc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ProblemRouter {

    private static final Logger logger = LoggerFactory.getLogger(ProblemRouter.class);

    private final ChatClient chatClient;
    private final ToolRegistryService toolRegistryService;

    public ProblemRouter(ChatClient chatClient, ToolRegistryService toolRegistryService) {
        this.chatClient = chatClient;
        this.toolRegistryService = toolRegistryService;
    }

    public String routeProblem(String problemDescription) {
        String toolContext = toolRegistryService.listToolDescriptions();
        var response = chatClient.prompt()
                .user("""
            You are a routing assistant.
            Your job is to choose the correct tool name for the given user problem.

            Available tools:
            promo_service - Handles promotion updates in the database.
            item_scan_issue - Handles creation of service central ticket in case an item is not scannable.
            (Respond ONLY with the tool name, e.g. 'promo_service'. Do NOT explain.)
            
            Problem: %s
            """.formatted(problemDescription))
                .call()
                .content();


        String toolName = response.trim()
                .replaceAll("[^a-zA-Z0-9_]", "")
                .toLowerCase();
        logger.info("AI response (tool decision): {}", toolName);

        if (toolRegistryService.hasTool(toolName)) {
            String result = toolRegistryService.callTool(toolName, problemDescription);
            logger.info("Tool '{}' executed successfully: {}", toolName, result);
            return result;
        } else {
            logger.warn("Unknown tool '{}'", toolName);
            return "Sorry, I couldn’t find a matching service for your request.";
        }
    }
}
