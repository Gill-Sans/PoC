package com.capgemini.dllpoc.controller;

import com.capgemini.dllpoc.model.CallData;
import com.capgemini.dllpoc.service.*;
import com.twilio.twiml.VoiceResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/twilio")
@CrossOrigin
public class TwilioController {

    private final TwilioCallFlowService callFlowService;
    private final CallDataService callDataService;

    public TwilioController(TwilioCallFlowService callFlowService, CallDataService callDataService) {
        this.callFlowService = callFlowService;
        this.callDataService = callDataService;
    }

    @PostMapping(value = "/voice", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> handleIncomingCall(@RequestParam String CallSid) {
        CallData data = callDataService.getOrCreate(CallSid, "en");
        String xml = callFlowService.askLanguageSelection(data);
        return ResponseEntity.ok(xml);
    }

    @PostMapping(value = "/language", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> handleLanguage(@RequestParam String Digits, @RequestParam String CallSid) {
        VoiceResponse response = callFlowService.handleLanguageSelection(CallSid, Digits);
        return ResponseEntity.ok(response.toXml());
    }

    @PostMapping(value = "/account", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> askAccount(@RequestParam String lang) {
        return ResponseEntity.ok(callFlowService.askAccount(lang));
    }

    @PostMapping(value = "/name", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> askName(@RequestParam String lang, @RequestParam String CallSid,
                                          @RequestParam(required = false) String Digits) {
        CallData data = callDataService.getOrCreate(CallSid, lang);
        data.setAccountNumber(Digits);
        return ResponseEntity.ok(callFlowService.askName(lang));
    }

    @PostMapping(value = "/problem", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> askProblem(@RequestParam String lang, @RequestParam String CallSid,
                                             @RequestParam(required = false) String SpeechResult) {
        CallData data = callDataService.getOrCreate(CallSid, lang);
        data.setName(SpeechResult);
        return ResponseEntity.ok(callFlowService.askProblem(lang));
    }

    @PostMapping(value = "/confirm", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> confirmDetails(@RequestParam String lang, @RequestParam String CallSid,
                                                 @RequestParam(required = false) String SpeechResult) {
        CallData data = callDataService.getOrCreate(CallSid, lang);
        data.setProblemDescription(SpeechResult);
        return ResponseEntity.ok(callFlowService.confirmDetails(lang, data));
    }

    @PostMapping(value = "/thanks", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> thankUser(@RequestParam String lang, @RequestParam String CallSid,
                                            @RequestParam(required = false) String SpeechResult) {
        CallData data = callDataService.get(CallSid);
        return ResponseEntity.ok(callFlowService.handleConfirmation(lang, SpeechResult, data));
    }
}
