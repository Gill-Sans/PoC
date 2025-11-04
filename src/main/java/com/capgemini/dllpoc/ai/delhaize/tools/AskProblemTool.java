package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.ai.delhaize.model.CallData;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class AskProblemTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private static final String ACTION_URL = "/twilio/process?lang=";

    public AskProblemTool(TwilioResponseBuilder twilioResponseBuilder) {
        this.twilioResponseBuilder = twilioResponseBuilder;
    }

    @Tool(description = "Generates Twilio XML to confirm the user's provided details (name, account number, and problem description) before proceeding to resolve the reported issue. Use this after collecting all necessary information from the user. Returns XML directly")
    public String askProblem(String lang, CallData data) {
        Say.Language sayLanguage = ToolLanguageUtil.getSayLanguage(lang);
        String confirmationMessage = getConfirmationMessage(lang, data);
        String actionUrl = ACTION_URL + lang;

        return twilioResponseBuilder.promptForUserInput(confirmationMessage, actionUrl, sayLanguage, true);
    }

    private String getConfirmationMessage(String lang, CallData data) {
        String template;
        if (lang == null || lang.equalsIgnoreCase("en")) {
            template = "You said your name is %s, your account number is %s, and your problem is: %s. Is that correct? Please say yes or no.";
        } else {
            switch (lang.toLowerCase()) {
                case "nl":
                case "dutch":
                    template = "U heeft gezegd dat uw naam %s is, uw rekeningnummer is %s, en uw probleem is: %s. Klopt dat? Zeg alstublieft ja of nee.";
                    break;
                case "fr":
                case "french":
                    template = "Vous avez dit que votre nom est %s, votre numéro de compte est %s, et votre problème est: %s. Est-ce correct? Veuillez dire oui ou non.";
                    break;
                default:
                    template = "You said your name is %s, your account number is %s, and your problem is: %s. Is that correct? Please say yes or no.";
            }
        }
        return String.format(template, data.name(), data.accountNumber(), data.problemDescription());
    }
}
