package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.ai.delhaize.model.CallData;
import com.capgemini.dllpoc.config.properties.LanguageProperties;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class HangupTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private final LanguageProperties languageProperties;
    private static final String ACTION_URL = "/twilio/process?lang=";

    public HangupTool(TwilioResponseBuilder twilioResponseBuilder, LanguageProperties languageProperties) {
        this.twilioResponseBuilder = twilioResponseBuilder;
        this.languageProperties = languageProperties;
    }

    @Tool(
            description = "Generates Twilio XML to gracefully end the call after completing the interaction. Use this as the final step in the voice flow when no further input is required. The response includes a farewell message and a <Hangup> command to terminate the call. Returns XML directly"
    )
    public String hangupTool(Say.Language language, String confirmation, CallData data) {
        if (confirmation != null && confirmation.toLowerCase().contains("yes")) {
            String template = ToolLanguageUtil.getMessage(languageProperties.getThankYou(), language);
            String message = String.format(template, data.name());

            return new VoiceResponse.Builder()
                    .say(new Say.Builder(message).language(language).build())
                    .hangup(new Hangup.Builder().build())
                    .build()
                    .toXml();
        }

        String retryMessage = ToolLanguageUtil.getMessage(languageProperties.getRetry(), language);
        String actionUrl = ACTION_URL + language;

        return twilioResponseBuilder.promptForUserInput(retryMessage, actionUrl, language, true);
    }

}
