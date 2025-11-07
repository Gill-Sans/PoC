package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import org.springframework.ai.tool.annotation.Tool;

public class AskLanguageTool {

    private final TwilioResponseBuilder twilioResponseBuilder;
    private static final String ACTION_URL = "/twilio/process";

    public AskLanguageTool(TwilioResponseBuilder twilioResponseBuilder) {
        this.twilioResponseBuilder = twilioResponseBuilder;
    }

    @Tool(description = "Returns Twilio XML to ask user to select language using DTMF. Use when starting conversation. Returns XML directly.")
    public String askLanguage() {
        return twilioResponseBuilder.buildLanguageSelectionPrompt(ACTION_URL);
    }
}
