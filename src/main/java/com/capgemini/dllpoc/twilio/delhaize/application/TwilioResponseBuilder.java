package com.capgemini.dllpoc.twilio.delhaize.application;

import com.capgemini.dllpoc.twilio.delhaize.ports.TwilioResponseBuilderUseCase;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Redirect;
import com.twilio.twiml.voice.Say;
import org.springframework.stereotype.Service;

@Service
public class TwilioResponseBuilder implements TwilioResponseBuilderUseCase {

    public Say say(String text, Say.Language lang) {
        return new Say.Builder(text)
                .voice(Say.Voice.POLLY_AMY)
                .language(lang)
                .build();
    }

    public String gatherXml(String message, String actionUrl, Say.Language language, boolean useSpeech) {
        String inputType = useSpeech ? "speech" : "dtmf";
        String speechTimeout = useSpeech ? " speechTimeout=\"5\"" : "";
        String sayLanguage = language == Say.Language.NL_NL ? "nl-NL" : "en-US";

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Response>\n" +
                "  <Gather speechModel=\"googlev2_telephony\" input=\"" + inputType + "\"" + speechTimeout + " timeout=\"5\" method=\"POST\" action=\"" + actionUrl + "\">\n" +
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