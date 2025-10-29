package com.capgemini.dllpoc;

import com.twilio.http.HttpMethod;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin
@RequestMapping("/twilio")
public class TwilioController {

    private static final Logger logger = LoggerFactory.getLogger(TwilioController.class);

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    private final CallDataService dataService;

    // === Simple in-memory store for active calls ===
    private static final Map<String, CallData> callDataMap = new ConcurrentHashMap<>();

    public TwilioController(CallDataService dataService) {
        this.dataService = dataService;
    }

    // Simple record to hold user data for now
    public static class CallData {
        public String lang;
        public String accountNumber;
        public String name;

        public CallData(String lang) {
            this.lang = lang;
        }

        @Override
        public String toString() {
            return "CallData{" +
                    "lang='" + lang + '\'' +
                    ", accountNumber='" + accountNumber + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private Say say(String text, Say.Language lang) {
        return new Say.Builder(text)
                .voice(Say.Voice.POLLY_AMY)
                .language(lang)
                .build();
    }

    // === 1️⃣ Incoming call ===
    @PostMapping(value = "/voice", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> handleIncomingCall(@RequestParam("CallSid") String callSid) {
        callSid = callSid.split(",")[0];
        logger.info("Incoming call: {}", callSid);

        // Create a blank CallData entry early
        callDataMap.put(callSid, new CallData("en"));

        Gather gather = new Gather.Builder()
                .numDigits(1)
                .action("/twilio/language")
                .method(HttpMethod.POST)
                .say(new Say.Builder("For English, press 1. Voor Nederlands, druk op 2.")
                        .language(Say.Language.EN_GB)
                        .voice(Say.Voice.POLLY_AMY)
                        .build())
                .build();

        VoiceResponse response = new VoiceResponse.Builder()
                .gather(gather)
                .say(new Say.Builder("We did not receive any input. Goodbye!")
                        .language(Say.Language.EN_GB)
                        .build())
                .build();

        return ResponseEntity.ok(response.toXml());
    }

    // === 2️⃣ Handle language selection ===
    @PostMapping(value = "/language", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> handleLanguage(@RequestParam("Digits") String digits,
                                                 @RequestParam("CallSid") String callSid) {
        callSid = callSid.split(",")[0];
        logger.info("Language selected: {} for CallSid {}", digits, callSid);

        String lang = digits.equals("2") ? "nl" : "en";
        callDataMap.computeIfAbsent(callSid, k -> new CallData(lang)).lang = lang;

        String nextUrl = "/twilio/account?lang=" + lang;

        VoiceResponse response = new VoiceResponse.Builder()
                .redirect(new Redirect.Builder(nextUrl).build())
                .build();

        return ResponseEntity.ok(response.toXml());
    }

    // === 3️⃣ Ask for store account number ===
    @PostMapping(value = "/account", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> askAccount(@RequestParam("lang") String lang,
                                             @RequestParam("CallSid") String callSid) {
        callSid = callSid.split(",")[0];
        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_GB;
        String message = lang.equals("nl")
                ? "Voer uw winkelrekeningnummer in, gevolgd door een hekje."
                : "Please enter your store account number, followed by the pound key.";

        Gather gather = new Gather.Builder()
                .finishOnKey("#")
                .action("/twilio/name?lang=" + lang)
                .method(HttpMethod.POST)
                .say(say(message, language))
                .build();

        VoiceResponse response = new VoiceResponse.Builder()
                .gather(gather)
                .build();

        return ResponseEntity.ok(response.toXml());
    }

    // === 4️⃣ Ask for name (speech input) ===
    @PostMapping(value = "/name", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> askName(@RequestParam("lang") String lang,
                                          @RequestParam("CallSid") String callSid,
                                          @RequestParam(value = "Digits", required = false) String accountNumber) {
        callSid = callSid.split(",")[0];
        logger.info("Account number for {}: {}", callSid, accountNumber);

        // Save account number
        callDataMap.computeIfAbsent(callSid, k -> new CallData(lang)).accountNumber = accountNumber;

        String baseUrl = "https://salma-enjambed-unmenially.ngrok-free.dev";
        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_GB;
        String message = lang.equals("nl")
                ? "Zeg alstublieft uw naam na de pieptoon."
                : "Please say your name after the beep.";

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Response>\n" +
                "  <Gather input=\"speech\" timeout=\"5\" speechTimeout=\"auto\" " +
                "action=\"" + baseUrl + "/twilio/thanks?lang=" + lang + "\" method=\"POST\">\n" +
                "    <Say language=\"" + (lang.equals("nl") ? "nl-NL" : "en-GB") + "\">" + message + "</Say>\n" +
                "  </Gather>\n" +
                "  <Say language=\"" + (lang.equals("nl") ? "nl-NL" : "en-GB") + "\">We did not receive your name. Goodbye!</Say>\n" +
                "</Response>";

        return ResponseEntity.ok(xml);
    }

    // === 5️⃣ Thank and hang up ===
    @PostMapping(value = "/thanks", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> thankUser(@RequestParam("lang") String lang,
                                            @RequestParam("CallSid") String callSid,
                                            @RequestParam(value = "SpeechResult", required = false) String name) {
        callSid = callSid.split(",")[0];
        logger.info("Name received for {}: {}", callSid, name);

        // Save name
        callDataMap.computeIfAbsent(callSid, k -> new CallData(lang)).name = name;

        CallData data = callDataMap.get(callSid);
        logger.info("✅ Full call data for {}: {}", callSid, data);

        dataService.handleCompletedCall(callSid, data);

        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_GB;
        String message = lang.equals("nl")
                ? "Bedankt " + (name != null ? name : "") + ". Tot ziens!"
                : "Thank you " + (name != null ? name : "") + ". Goodbye!";

        VoiceResponse response = new VoiceResponse.Builder()
                .say(say(message, language))
                .hangup(new Hangup.Builder().build())
                .build();

        logger.info("Current call data map: {}", callDataMap);
        return ResponseEntity.ok(response.toXml());
    }
}