package com.capgemini.dllpoc.ai.delhaize.tools;

import com.capgemini.dllpoc.ai.delhaize.model.CallData;
import com.capgemini.dllpoc.twilio.delhaize.application.TwilioResponseBuilder;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Say;
import org.springframework.ai.tool.annotation.Tool;

public class HangupTool {

    private final TwilioResponseBuilder twilioResponseBuilder;

    public HangupTool(TwilioResponseBuilder twilioResponseBuilder) {
        this.twilioResponseBuilder = twilioResponseBuilder;
    }

    @Tool(
            description = "Generates Twilio XML to gracefully end the call after completing the interaction. Use this as the final step in the voice flow when no further input is required. The response includes a farewell message and a <Hangup> command to terminate the call. Returns XML directly"
    )
    public String hangupTool(String lang, String confirmation, CallData data) {
        Say.Language language = getSayLanguage(lang);

        if (confirmation != null && confirmation.toLowerCase().contains("yes")) {

            String message = (lang.equalsIgnoreCase("nl")
                    ? "Bedankt " + data.name()
                    : "Thank you " + data.name());

            return new VoiceResponse.Builder()
                    .say(new Say.Builder(message).language(language).build())
                    .hangup(new Hangup.Builder().build())
                    .build()
                    .toXml();
        }

        String retryMessage = (lang.equalsIgnoreCase("nl")
                ? "Laten we het opnieuw proberen. Beschrijf uw probleem opnieuw."
                : "Let's try again. Please describe your problem again.");

        String actionUrl = "/twilio/problem?lang=" + lang;

        return twilioResponseBuilder.gatherXml(retryMessage, actionUrl, language, true);
    }

    private Say.Language getSayLanguage(String lang) {
        if (lang == null) {
            return Say.Language.EN_US;
        }
        return switch (lang.toLowerCase()) {
            case "nl", "dutch" -> Say.Language.NL_NL;
            case "fr", "french" -> Say.Language.FR_FR;
            default -> Say.Language.EN_US;
        };
    }

}
