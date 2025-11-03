package com.capgemini.dllpoc.ai.delhaize.tools;

import com.twilio.twiml.voice.Say;

public class ToolLanguageUtil {
    public static Say.Language getSayLanguage(String lang) {
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
