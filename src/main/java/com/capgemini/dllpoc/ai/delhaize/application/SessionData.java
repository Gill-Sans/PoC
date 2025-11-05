package com.capgemini.dllpoc.ai.delhaize.application;

import com.twilio.twiml.voice.Say;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionData {

    private Say.Language language;
    private String storeNumber;
}
