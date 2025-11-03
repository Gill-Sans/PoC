package com.capgemini.dllpoc.twilio.ports.in;

public interface ToolRegistryUseCase {
    void registerTools();
    boolean hasTool(String name);
    String callTool(String name, String input);
    String listToolDescriptions();

    //don't know if these should be in the interface or not as they are tools for Spring AI to use
    String updatePromo(String input);
    String createItemScanTicket(String input);
}
