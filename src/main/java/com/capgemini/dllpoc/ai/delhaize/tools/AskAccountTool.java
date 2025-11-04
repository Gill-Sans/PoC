package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class AskAccountTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private static final String ACTION_URL = "/twilio/process?lang=";

    public AskAccountTool(TwilioResponseBuilder twilioResponseBuilder) {
        this.twilioResponseBuilder = twilioResponseBuilder;
    }

    @Tool(description = "Returns Twilio XML to ask user to enter account number using DTMF. Use after name collected. Returns XML directly.")
    public String askAccount(String language) {
        String message = getAccountPrompt(language);
        String actionUrl = ACTION_URL + language;
        Say.Language sayLanguage = ToolLanguageUtil.getSayLanguage(language);

        return twilioResponseBuilder.promptForUserInput(message, actionUrl, sayLanguage, false);
    }

    private String getAccountPrompt(String language) {
        if (language == null) {
            return "Please enter your store account number, followed by the pound key.";
        }

        return switch (language.toLowerCase()) {
            case "nl", "dutch" -> "Voer uw winkelrekeningnummer in, gevolgd door een hekje.";
            case "fr", "french" -> "Veuillez entrer votre numéro de compte magasin, suivi de la touche dièse.";
            default -> "Please enter your store account number, followed by the pound key.";
        };
    }
}
