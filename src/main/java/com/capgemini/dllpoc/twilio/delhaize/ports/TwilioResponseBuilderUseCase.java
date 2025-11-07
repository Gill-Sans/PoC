package com.capgemini.dllpoc.twilio.delhaize.ports;

import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;

public interface TwilioResponseBuilderUseCase {
    Say createSpeechElement(String text, Say.Language lang);
    String promptForUserInput(String message, String actionUrl, Say.Language language, boolean useSpeech);
    String buildLanguageSelectionPrompt(String actionUrl);
    VoiceResponse endCallWithMessage(String message, Say.Language lang);
    VoiceResponse continueToNextStep(String url);
}