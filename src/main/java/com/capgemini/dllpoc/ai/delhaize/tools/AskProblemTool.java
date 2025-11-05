package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.ai.delhaize.model.CallData;
import com.capgemini.dllpoc.config.properties.LanguageProperties;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

@Slf4j
public class AskProblemTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private final LanguageProperties languageProperties;
    private static final String ACTION_URL = "/twilio/process?lang=";

    public AskProblemTool(TwilioResponseBuilder twilioResponseBuilder, LanguageProperties languageProperties) {
        this.twilioResponseBuilder = twilioResponseBuilder;
        this.languageProperties = languageProperties;
    }

    @Tool(description = "Generates Twilio XML to ask the user to describe the problem they are facing. Use this after collecting the language, the store name and store number. Returns XML directly")
    public String askProblem(Say.Language language, CallData data) {
        String message = ToolLanguageUtil.getMessage(languageProperties.getAskProblem(), language);
        String actionUrl = ACTION_URL + language;
        log.info("Asking problem confirmation with message");

        return twilioResponseBuilder.promptForUserInput(message, actionUrl, language, true);
    }
}
