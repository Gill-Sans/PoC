package com.capgemini.dllpoc.ai.delhaize.tools;

import com.twilio.twiml.voice.Say;
import java.util.Map;

public class ToolLanguageUtil {

    public static String getMessage(Map<Say.Language, String> messageMap, Say.Language language) {
        return messageMap.get(language);
    }
}
