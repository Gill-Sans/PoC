package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.ai.delhaize.model.CallData;
import com.capgemini.dllpoc.config.properties.LanguageProperties;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.voice.Say;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

@Slf4j
public class ConfirmProblemTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private final LanguageProperties languageProperties;
    private static final String ACTION_URL = "/twilio/process?lang=";

    public ConfirmProblemTool(TwilioResponseBuilder twilioResponseBuilder, LanguageProperties languageProperties) {
        this.twilioResponseBuilder = twilioResponseBuilder;
        this.languageProperties = languageProperties;
    }

    @Tool(description = "Generates Twilio XML to confirm the user's provided details (name, store number, and problem description) before proceeding to resolve the reported issue. Use this after collecting all necessary information from the user. Returns XML directly")
    public String confirmProblem(Say.Language language, CallData data) {
        String template = ToolLanguageUtil.getMessage(languageProperties.getConfirmProblem(), language);
        String confirmationMessage = String.format(template, data.name(), data.accountNumber(), data.problemDescription());
        String actionUrl = ACTION_URL + language;
        log.info("Asking problem confirmation with message: {}", confirmationMessage);

        return twilioResponseBuilder.promptForUserInput(confirmationMessage, actionUrl, language, true);
    }
}
