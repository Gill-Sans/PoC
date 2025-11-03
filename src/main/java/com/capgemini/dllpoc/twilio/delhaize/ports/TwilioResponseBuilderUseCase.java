package com.capgemini.dllpoc.twilio.delhaize.ports;

import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;

public interface TwilioResponseBuilderUseCase {
    Say say(String text, Say.Language lang);
    String gatherXml(String message, String actionUrl, Say.Language language, boolean useSpeech);
    VoiceResponse sayAndHangup(String message, Say.Language lang);
    VoiceResponse redirect(String url);
}