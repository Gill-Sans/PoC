package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.ai.delhaize.model.CallData;
import com.capgemini.dllpoc.config.properties.LanguageProperties;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class AskProblemTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private final LanguageProperties languageProperties;
    private static final String ACTION_URL = "/twilio/process?lang=";

    public AskProblemTool(TwilioResponseBuilder twilioResponseBuilder, LanguageProperties languageProperties) {
        this.twilioResponseBuilder = twilioResponseBuilder;
        this.languageProperties = languageProperties;
    }

    @Tool(description = "Generates Twilio XML to confirm the user's provided details (name, account number, and problem description) before proceeding to resolve the reported issue. Use this after collecting all necessary information from the user. Returns XML directly")
    public String askProblem(Say.Language language, CallData data) {
        String template = ToolLanguageUtil.getMessage(languageProperties.getConfirmProblem(), language);
        String confirmationMessage = String.format(template, data.name(), data.accountNumber(), data.problemDescription());
        String actionUrl = ACTION_URL + language;

        return twilioResponseBuilder.promptForUserInput(confirmationMessage, actionUrl, language, true);
    }
}
