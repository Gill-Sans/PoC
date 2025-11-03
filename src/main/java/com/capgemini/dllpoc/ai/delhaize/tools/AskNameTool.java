package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class AskNameTool {

    private final TwilioResponseBuilder twilioResponseBuilder;

    public AskNameTool(TwilioResponseBuilder twilioResponseBuilder) {
        this.twilioResponseBuilder = twilioResponseBuilder;
    }

    @Tool(description = "Returns Twilio XML to ask user to say their name. Use after language selected. Returns XML directly.")
    public String askName(String language) {
        String message = getNamePrompt(language);
        String actionUrl = "/twilio/process?lang=" + language;
        Say.Language sayLanguage = getSayLanguage(language);

        return twilioResponseBuilder.gatherXml(message, actionUrl, sayLanguage, true);
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
}
