package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.ai.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class TwilioResponseTool {

    private final TwilioResponseBuilder twilioResponseBuilder;

    public TwilioResponseTool(TwilioResponseBuilder twilioResponseBuilder) {
        this.twilioResponseBuilder = twilioResponseBuilder;
    }

    @Tool(description = "Generate Twilio response based on the conversation flow step. Use this tool for all Twilio responses including language selection, name input, account input, etc.")
    public String generateTwilioResponse(String flowStep, String language, String message, String actionUrl, boolean useSpeech, boolean useDtmf, int numDigits) {
        Say.Language sayLanguage = getSayLanguage(language);
        
        if (useDtmf || useSpeech) {
            // Para inputs DTMF ou de voz
            return twilioResponseBuilder.gatherXml(message, actionUrl, sayLanguage, useSpeech);
        } else {
            // Para respostas simples sem input - usar sayAndHangup
            return twilioResponseBuilder.sayAndHangup(message, sayLanguage).toXml();
        }
    }

    @Tool(description = "Returns Twilio XML to ask user to select language using DTMF. Use when starting conversation. Returns XML directly.")
    public String askLanguage() {
        String message = "Welcome to our service. Press 1 for English, 2 for French, 3 for Dutch.";
        String actionUrl = "/twilio/process";
        Say.Language language = Say.Language.EN_US;
        
        return twilioResponseBuilder.gatherXml(message, actionUrl, language, false);
    }

    @Tool(description = "Returns Twilio XML to ask user to say their name. Use after language selected. Returns XML directly.")
    public String askName(String language) {
        String message = getNamePrompt(language);
        String actionUrl = "/twilio/process?lang=" + language;
        Say.Language sayLanguage = getSayLanguage(language);
        
        return twilioResponseBuilder.gatherXml(message, actionUrl, sayLanguage, true);
    }

    @Tool(description = "Returns Twilio XML to ask user to enter account number using DTMF. Use after name collected. Returns XML directly.")
    public String askAccount(String language) {
        String message = getAccountPrompt(language);
        String actionUrl = "/twilio/account?lang=" + language;
        Say.Language sayLanguage = getSayLanguage(language);
        
        return twilioResponseBuilder.gatherXml(message, actionUrl, sayLanguage, false);
    }

    private Say.Language getSayLanguage(String language) {
        if (language == null) {
            return Say.Language.EN_US;
        }
        
        return switch (language.toLowerCase()) {
            case "nl", "dutch" -> Say.Language.NL_NL;
            case "fr", "french" -> Say.Language.FR_FR;
            default -> Say.Language.EN_US;
        };
    }

    private String getNamePrompt(String language) {
        if (language == null) {
            return "Please say your name.";
        }
        
        return switch (language.toLowerCase()) {
            case "nl", "dutch" -> "Zeg alstublieft uw naam.";
            case "fr", "french" -> "Veuillez dire votre nom.";
            default -> "Please say your name.";
        };
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