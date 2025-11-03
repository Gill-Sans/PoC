package com.capgemini.dllpoc.twilio.application;

import com.capgemini.dllpoc.twilio.model.CallData;
import com.capgemini.dllpoc.twilio.ports.in.TwilioCallFlowUseCase;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Say;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioCallFlowService implements TwilioCallFlowUseCase {

    private static final Logger logger = LoggerFactory.getLogger(TwilioCallFlowService.class);

    private final CallDataService callDataService;
    private final TwilioResponseBuilder responseBuilder;
    private final ProblemRouter problemRouter;

    @Value("${app.base.url}")
    private String baseUrl;

    public TwilioCallFlowService(CallDataService callDataService, TwilioResponseBuilder responseBuilder, ProblemRouter problemRouter) {
        this.callDataService = callDataService;
        this.responseBuilder = responseBuilder;
        this.problemRouter = problemRouter;
    }

    public String askLanguageSelection(CallData data) {
        Say.Language language = Say.Language.EN_US;
        String message = "For English, press 1. Voor Nederlands, druk op 2.";

        return responseBuilder.gatherXml(
                message,
                baseUrl + "/twilio/language",
                language,
                false
        );
    }

    public VoiceResponse handleLanguageSelection(String callSid, String digits) {
        String lang = digits.equals("2") ? "nl" : "en";
        callDataService.getOrCreate(callSid, lang).setLang(lang);
        return responseBuilder.redirect(baseUrl + "/twilio/account?lang=" + lang);
    }

    public String askAccount(String lang) {
        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
        String message = lang.equals("nl")
                ? "Voer uw winkelrekeningnummer in, gevolgd door een hekje."
                : "Please enter your store account number, followed by the pound key.";
        return responseBuilder.gatherXml(message, baseUrl + "/twilio/name?lang=" + lang, language, false);
    }

    public String askName(String lang) {
        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
        String message = lang.equals("nl") ? "Zeg alstublieft uw naam." : "Please say your name.";
        return responseBuilder.gatherXml(message, baseUrl + "/twilio/problem?lang=" + lang, language, true);
    }

    public String askProblem(String lang) {
        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
        String message = lang.equals("nl")
                ? "Kunt u alstublieft kort uitleggen wat het probleem is?"
                : "Please briefly explain the problem you are experiencing.";
        return responseBuilder.gatherXml(message, baseUrl + "/twilio/confirm?lang=" + lang, language, true);
    }

    public String confirmDetails(String lang, CallData data) {
        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
        String confirmationMessage = lang.equals("nl")
                ? String.format("U heeft gezegd dat uw naam %s is, uw rekeningnummer is %s, en uw probleem is: %s. Klopt dat? Zeg alstublieft ja of nee.",
                data.getName(), data.getAccountNumber(), data.getProblemDescription())
                : String.format("You said your name is %s, your account number is %s, and your problem is: %s. Is that correct?",
                data.getName(), data.getAccountNumber(), data.getProblemDescription());
        return responseBuilder.gatherXml(confirmationMessage, baseUrl + "/twilio/thanks?lang=" + lang, language, true);
    }


    public String handleConfirmation(String lang, String confirmation, CallData data) {
        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;

        if (confirmation != null && confirmation.toLowerCase().contains("yes")) {
            String aiResponse = problemRouter.routeProblem(data.getProblemDescription());
            String message = (lang.equals("nl")
                    ? "Bedankt " + data.getName() + ". " + aiResponse
                    : "Thank you " + data.getName() + ". " + aiResponse);

            return new VoiceResponse.Builder()
                    .say(new Say.Builder(message).language(language).build())
                    .hangup(new Hangup.Builder().build())
                    .build()
                    .toXml();
        }

        String retryMessage = (lang.equals("nl")
                ? "Laten we het opnieuw proberen. Beschrijf uw probleem opnieuw."
                : "Let's try again. Please describe your problem again.");

        return responseBuilder.gatherXml(
                retryMessage,
                baseUrl + "/twilio/problem?lang=" + lang,
                language,
                true
        );
    }

}
