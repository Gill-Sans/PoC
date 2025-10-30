package com.capgemini.dllpoc.service;

import com.twilio.http.HttpMethod;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.*;
import org.springframework.stereotype.Service;

@Service
public class TwilioResponseBuilder {

    private Say say(String text, Say.Language lang) {
        return new Say.Builder(text)
                .voice(Say.Voice.POLLY_AMY)
                .language(lang)
                .build();
    }

    public String gatherXml(String message, String actionUrl, Say.Language language, boolean useSpeech) {
        String inputType = useSpeech ? "speech" : "dtmf";
        String speechTimeout = useSpeech ? " speechTimeout=\"auto\"" : "";
        String sayLanguage = language == Say.Language.NL_NL ? "nl-NL" : "en-US";

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Response>\n" +
                "  <Gather input=\"" + inputType + "\"" + speechTimeout + " timeout=\"5\" method=\"POST\" action=\"" + actionUrl + "\">\n" +
                "    <Say language=\"" + sayLanguage + "\">" + message + "</Say>\n" +
                "  </Gather>\n" +
                "  <Say language=\"" + sayLanguage + "\">We did not receive your response. Goodbye!</Say>\n" +
                "</Response>";
    }

    public VoiceResponse sayAndHangup(String message, Say.Language lang) {
        return new VoiceResponse.Builder()
                .say(say(message, lang))
                .hangup(new Hangup.Builder().build())
                .build();
    }

    public VoiceResponse redirect(String url) {
        return new VoiceResponse.Builder()
                .redirect(new Redirect.Builder(url).build())
                .build();
    }
}
