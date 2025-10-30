package com.capgemini.dllpoc.service;

import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class ToolRegistryService {

    private final PromoService promoService;
    private final ItemScanService itemScanService;

    private final Map<String, Method> toolMap = new HashMap<>();

    public ToolRegistryService(PromoService promoService, ItemScanService itemScanService) {
        this.promoService = promoService;
        this.itemScanService = itemScanService;
        registerTools();
    }

    private void registerTools() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Tool.class)) {
                Tool tool = method.getAnnotation(Tool.class);
                toolMap.put(tool.name(), method);
            }
        }
    }

    public boolean hasTool(String name) {
        return toolMap.containsKey(name);
    }

    public String callTool(String name, String input) {
        try {
            Method method = toolMap.get(name);
            if (method == null) {
                return "No tool found for name: " + name;
            }
            return (String) method.invoke(this, input);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error calling tool: " + e.getMessage();
        }
    }

    public String listToolDescriptions() {
        return toolMap.entrySet().stream()
                .map(e -> e.getKey() + " - " + e.getValue().getAnnotation(Tool.class).description())
                .reduce("", (a, b) -> a + "\n" + b);
    }


    @Tool(name = "promo_service", description = "Handles promotion updates in the database.")
    public String updatePromo(String input) {
        return promoService.handlePromoUpdate(input);
    }

    @Tool(name = "item_scan_issue", description = "Creates a ticket for service central if an item is not scannable.")
    public String createItemScanTicket(String input) {
        return itemScanService.handleItemScanIssue(input);
    }
}
