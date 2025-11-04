package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.config.properties.LanguageProperties;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class AskAccountTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private final LanguageProperties languageProperties;
    private static final String ACTION_URL = "/twilio/process?lang=";

    public AskAccountTool(TwilioResponseBuilder twilioResponseBuilder, LanguageProperties languageProperties) {
        this.twilioResponseBuilder = twilioResponseBuilder;
        this.languageProperties = languageProperties;
    }

    @Tool(description = "Returns Twilio XML to ask user to enter account number using DTMF. Use after name collected. Returns XML directly.")
    public String askAccount(Say.Language language) {
        String message = ToolLanguageUtil.getMessage(languageProperties.getAskAccount(), language);
        String actionUrl = ACTION_URL + language;

        return twilioResponseBuilder.promptForUserInput(message, actionUrl, language, false);
    }
}
