//package com.capgemini.dllpoc;
//
//import com.twilio.http.HttpMethod;
//import com.twilio.twiml.VoiceResponse;
//import com.twilio.twiml.voice.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@RestController
//@CrossOrigin
//@RequestMapping("/twilio")
//public class TwilioController {
//
//    private static final Logger logger = LoggerFactory.getLogger(TwilioController.class);
//
//    @Value("${twilio.account.sid}")
//    private String accountSid;
//
//    @Value("${twilio.auth.token}")
//    private String authToken;
//
//    @Autowired
//    private ProblemRouter problemRouter;
//
//    private static final Map<String, CallData> callDataMap = new ConcurrentHashMap<>();
//
//
//    public static class CallData {
//        public String lang;
//        public String accountNumber;
//        public String name;
//        public String problemDescription;
//
//        public CallData(String lang) {
//            this.lang = lang;
//        }
//
//        @Override
//        public String toString() {
//            return "CallData{" +
//                    "lang='" + lang + '\'' +
//                    ", accountNumber='" + accountNumber + '\'' +
//                    ", name='" + name + '\'' +
//                    ", problemDescription='" + problemDescription + '\'' +
//                    '}';
//        }
//    }
//
//    private Say say(String text, Say.Language lang) {
//        return new Say.Builder(text)
//                .voice(Say.Voice.POLLY_AMY)
//                .language(lang)
//                .build();
//    }
//
//    // === 1️⃣ Incoming call ===
//    @PostMapping(value = "/voice", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> handleIncomingCall(@RequestParam("CallSid") String callSid) {
//        callSid = callSid.split(",")[0];
//        logger.info("Incoming call: {}", callSid);
//        callDataMap.put(callSid, new CallData("en"));
//
//        Gather gather = new Gather.Builder()
//                .numDigits(1)
//                .action("/twilio/language")
//                .method(HttpMethod.POST)
//                .say(new Say.Builder("For English, press 1. Voor Nederlands, druk op 2.")
//                        .language(Say.Language.EN_US)
//                        .voice(Say.Voice.POLLY_AMY)
//                        .build())
//                .build();
//
//        VoiceResponse response = new VoiceResponse.Builder()
//                .gather(gather)
//                .say(new Say.Builder("We did not receive any input. Goodbye!")
//                        .language(Say.Language.EN_US)
//                        .build())
//                .build();
//
//        return ResponseEntity.ok(response.toXml());
//    }
//
//    // === 2️⃣ Handle language selection ===
//    @PostMapping(value = "/language", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> handleLanguage(@RequestParam("Digits") String digits,
//                                                 @RequestParam("CallSid") String callSid) {
//        callSid = callSid.split(",")[0];
//        logger.info("Language selected: {} for CallSid {}", digits, callSid);
//
//        String lang = digits.equals("2") ? "nl" : "en";
//        callDataMap.computeIfAbsent(callSid, k -> new CallData(lang)).lang = lang;
//
//        String nextUrl = "/twilio/account?lang=" + lang;
//
//        VoiceResponse response = new VoiceResponse.Builder()
//                .redirect(new Redirect.Builder(nextUrl).build())
//                .build();
//
//        return ResponseEntity.ok(response.toXml());
//    }
//
//    // === 3️⃣ Ask for store account number ===
//    @PostMapping(value = "/account", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> askAccount(@RequestParam("lang") String lang,
//                                             @RequestParam("CallSid") String callSid) {
//        callSid = callSid.split(",")[0];
//        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
//        String message = lang.equals("nl")
//                ? "Voer uw winkelrekeningnummer in, gevolgd door een hekje."
//                : "Please enter your store account number, followed by the pound key.";
//
//        Gather gather = new Gather.Builder()
//                .finishOnKey("#")
//                .action("/twilio/name?lang=" + lang)
//                .method(HttpMethod.POST)
//                .say(say(message, language))
//                .build();
//
//        VoiceResponse response = new VoiceResponse.Builder()
//                .gather(gather)
//                .build();
//
//        return ResponseEntity.ok(response.toXml());
//    }
//
//    // === 4️⃣ Ask for name (speech input) ===
//    @PostMapping(value = "/name", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> askName(@RequestParam("lang") String lang,
//                                          @RequestParam("CallSid") String callSid,
//                                          @RequestParam(value = "Digits", required = false) String accountNumber) {
//        callSid = callSid.split(",")[0];
//        logger.info("Account number for {}: {}", callSid, accountNumber);
//        callDataMap.computeIfAbsent(callSid, k -> new CallData(lang)).accountNumber = accountNumber;
//
//        String baseUrl = "https://salma-enjambed-unmenially.ngrok-free.dev";
//        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
//        String message = lang.equals("nl")
//                ? "Zeg alstublieft uw naam."
//                : "Please say your name.";
//
//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<Response>\n" +
//                "  <Gather input=\"speech\" timeout=\"5\" speechTimeout=\"auto\" " +
//                "action=\"" + baseUrl + "/twilio/problem?lang=" + lang + "\" method=\"POST\">\n" +
//                "    <Say language=\"" + (lang.equals("nl") ? "nl-NL" : "en-GB") + "\">" + message + "</Say>\n" +
//                "  </Gather>\n" +
//                "  <Say language=\"" + (lang.equals("nl") ? "nl-NL" : "en-GB") + "\">We did not receive your name. Goodbye!</Say>\n" +
//                "</Response>";
//
//        return ResponseEntity.ok(xml);
//    }
//
//    // === 5️⃣ Ask user to explain their problem ===
//    @PostMapping(value = "/problem", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> askProblem(@RequestParam("lang") String lang,
//                                             @RequestParam("CallSid") String callSid,
//                                             @RequestParam(value = "SpeechResult", required = false) String name) {
//        callSid = callSid.split(",")[0];
//        logger.info("Name received for {}: {}", callSid, name);
//
//        callDataMap.computeIfAbsent(callSid, k -> new CallData(lang)).name = name;
//
//        String baseUrl = "https://salma-enjambed-unmenially.ngrok-free.dev";
//        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
//        String message = lang.equals("nl")
//                ? "Kunt u alstublieft kort uitleggen wat het probleem is?"
//                : "Please briefly explain the problem you are experiencing.";
//
//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<Response>\n" +
//                "  <Gather input=\"speech\" timeout=\"10\" speechTimeout=\"auto\" " +
//                "action=\"" + baseUrl + "/twilio/confirm?lang=" + lang + "\" method=\"POST\">\n" +
//                "    <Say language=\"" + (lang.equals("nl") ? "nl-NL" : "en-GB") + "\">" + message + "</Say>\n" +
//                "  </Gather>\n" +
//                "  <Say language=\"" + (lang.equals("nl") ? "nl-NL" : "en-GB") + "\">We did not receive your response. Goodbye!</Say>\n" +
//                "</Response>";
//
//        return ResponseEntity.ok(xml);
//    }
//
//    // === 6️⃣ Confirm details ===
//    @PostMapping(value = "/confirm", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> confirmDetails(@RequestParam("lang") String lang,
//                                                 @RequestParam("CallSid") String callSid,
//                                                 @RequestParam(value = "SpeechResult", required = false) String problemDescription) {
//        callSid = callSid.split(",")[0];
//        logger.info("Problem description for {}: {}", callSid, problemDescription);
//
//        CallData data = callDataMap.computeIfAbsent(callSid, k -> new CallData(lang));
//        data.problemDescription = problemDescription;
//
//        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
//
//        String confirmationMessage = lang.equals("nl")
//                ? String.format("U heeft gezegd dat uw naam %s is, uw rekeningnummer is %s, en uw probleem is: %s. Klopt dat? Zeg alstublieft ja of nee.",
//                data.name != null ? data.name : "onbekend",
//                data.accountNumber != null ? data.accountNumber : "onbekend",
//                data.problemDescription != null ? data.problemDescription : "onbekend")
//                : String.format("You said your name is %s, your account number is %s, and your problem is: %s. Is that correct?",
//                data.name != null ? data.name : "unknown",
//                data.accountNumber != null ? data.accountNumber : "unknown",
//                data.problemDescription != null ? data.problemDescription : "unknown");
//
//        String baseUrl = "https://salma-enjambed-unmenially.ngrok-free.dev";
//
//        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<Response>\n" +
//                "  <Gather input=\"speech\" timeout=\"5\" speechTimeout=\"auto\" " +
//                "action=\"" + baseUrl + "/twilio/thanks?lang=" + lang + "\" method=\"POST\">\n" +
//                "    <Say language=\"" + (lang.equals("nl") ? "nl-NL" : "en-GB") + "\">" + confirmationMessage + "</Say>\n" +
//                "  </Gather>\n" +
//                "  <Say language=\"" + (lang.equals("nl") ? "nl-NL" : "en-GB") + "\">We did not receive a response. Goodbye!</Say>\n" +
//                "</Response>";
//
//        return ResponseEntity.ok(xml);
//    }
//
//
//    // === 7️⃣ Thank, route via AI, and hang up ===
//    @PostMapping(value = "/thanks", produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<String> thankUser(@RequestParam("lang") String lang,
//                                            @RequestParam("CallSid") String callSid,
//                                            @RequestParam(value = "SpeechResult", required = false) String confirmation) {
//        callSid = callSid.split(",")[0];
//        logger.info("Confirmation for {}: {}", callSid, confirmation);
//
//        CallData data = callDataMap.get(callSid);
//        if (data == null) {
//            logger.warn("No call data found for {}", callSid);
//            return ResponseEntity.ok("<Response><Say>No call data found. Goodbye!</Say><Hangup/></Response>");
//        }
//
//        Say.Language language = lang.equals("nl") ? Say.Language.NL_NL : Say.Language.EN_US;
//
//        if (confirmation != null && confirmation.toLowerCase().contains("yes")) {
//            logger.info("Caller confirmed details. Proceeding with AI routing...");
//
//            String aiResponse = problemRouter.routeProblem(data.problemDescription);
//            logger.info("AI response: {}", aiResponse);
//
//            String message = lang.equals("nl")
//                    ? "Bedankt " + (data.name != null ? data.name : "") + ". " + aiResponse
//                    : "Thank you " + (data.name != null ? data.name : "") + ". " + aiResponse;
//
//            VoiceResponse response = new VoiceResponse.Builder()
//                    .say(say(message, language))
//                    .hangup(new Hangup.Builder().build())
//                    .build();
//
//            logger.info("✅ Current call data map: {}", callDataMap);
//            return ResponseEntity.ok(response.toXml());
//
//        } else {
//            logger.info("Caller did not confirm. Asking to try again.");
//
//            VoiceResponse retryResponse = new VoiceResponse.Builder()
//                    .say(say("Let's try again. Please describe your problem.", language))
//                    .redirect(new Redirect.Builder("/twilio/confirm?lang=" + lang).build())
//                    .build();
//
//            return ResponseEntity.ok(retryResponse.toXml());
//        }
//    }
//
//}
