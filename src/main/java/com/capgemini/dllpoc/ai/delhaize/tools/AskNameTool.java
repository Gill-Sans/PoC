package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.config.properties.LanguageProperties;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class AskNameTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private final LanguageProperties languageProperties;
    private static final String ACTION_URL = "/twilio/process?lang=";

    public AskNameTool(TwilioResponseBuilder twilioResponseBuilder, LanguageProperties languageProperties) {
        this.twilioResponseBuilder = twilioResponseBuilder;
        this.languageProperties = languageProperties;
    }

    @Tool(description = "Returns Twilio XML to ask user to say their name. Use after language selected. Returns XML directly.")
    public String askName(Say.Language language) {
        String message = ToolLanguageUtil.getMessage(languageProperties.getAskName(), language);
        String actionUrl = ACTION_URL + language;

        return twilioResponseBuilder.promptForUserInput(message, actionUrl, language, true);
    }
}
