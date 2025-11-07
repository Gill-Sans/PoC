package com.capgemini.dllpoc.twilio.delhaize.application;

import com.capgemini.dllpoc.config.properties.LanguageProperties;
import com.capgemini.dllpoc.twilio.delhaize.ports.TwilioResponseBuilderUseCase;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Redirect;
import com.twilio.twiml.voice.Say;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TwilioResponseBuilder implements TwilioResponseBuilderUseCase {

    private final LanguageProperties languageProperties;

    public Say createSpeechElement(String text, Say.Language lang) {
        return new Say.Builder(text)
                .voice(Say.Voice.POLLY_AMY)
                .language(lang)
                .build();
    }

    public String promptForUserInput(String message, String actionUrl, Say.Language language, boolean useSpeech) {
        String timeoutMessage = languageProperties.getTimeout().getOrDefault(
                language,
                "We did not receive your response. Goodbye!"
        );

        return buildUserInputPromptXml(message, actionUrl, language, useSpeech, timeoutMessage);
    }

    public VoiceResponse endCallWithMessage(String message, Say.Language lang) {
        return new VoiceResponse.Builder()
                .say(createSpeechElement(message, lang))
                .hangup(new Hangup.Builder().build())
                .build();
    }

    public VoiceResponse continueToNextStep(String url) {
        return new VoiceResponse.Builder()
                .redirect(new Redirect.Builder(url).build())
                .build();
    }

    private String buildUserInputPromptXml(String message, String actionUrl, Say.Language language, boolean useSpeech, String timeoutMessage) {
        String inputType = useSpeech ? "speech" : "dtmf";
        String speechTimeout = useSpeech ? "speechTimeout=\"2\"" : "";
        String promptTimeout = " timeout=\"20\"";

        return  "<Response>\n" +
                "  <Gather speechModel=\"googlev2_telephony\" input=\"" + inputType + "\" " + speechTimeout + " method=\"POST\" action=\"" + actionUrl + "\">\n" +
                "    <Say language=\"" + language + "\">" + message + "</Say>\n" +
                "  </Gather>\n" +
                "  <Say language=\"" + language + "\">" + timeoutMessage + "</Say>\n" +
                "</Response>";
    }

    public String buildLanguageSelectionPrompt(String actionUrl) {
        StringBuilder gatherContent = new StringBuilder();

        // Build multiple <Say> elements, each with their own language
        // Selection structure: digit -> { languageCode -> message }
        languageProperties.getSelection()
                .forEach((digit, languageMap) -> languageMap
                        .forEach((languageCode, message) -> gatherContent
                                .append("    <Say language=\"")
                                .append(languageCode)
                                .append("\">")
                                .append(message)
                                .append("</Say>\n")
                        ));

        // Build timeout with multiple <Say> elements for each language
        StringBuilder timeoutContent = new StringBuilder();
        languageProperties.getTimeout().forEach((languageCode, message) -> timeoutContent
                .append("  <Say language=\"")
                .append(languageCode)
                .append("\">")
                .append(message)
                .append("</Say>\n")
        );

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Response>\n" +
                "  <Gather speechModel=\"googlev2_telephony\" input=\"dtmf\" numDigits=\"1\" timeout=\"5\" method=\"POST\" action=\"" + actionUrl + "\">\n" +
                gatherContent +
                "  </Gather>\n" +
                timeoutContent +
                "</Response>";
    }

    public Map<Integer, Say.Language> getLanguageDigitMapping() {
        return buildLanguageDigitMapping();
    }

    private Map<Integer, Say.Language> buildLanguageDigitMapping() {
        Map<Integer, Say.Language> mapping = new LinkedHashMap<>();

        languageProperties.getSelection().forEach((digit, languageMap) -> {
            Say.Language language = languageMap.keySet().iterator().next();
            mapping.put(digit, language);
        });

        return mapping;
    }
}