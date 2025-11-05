package com.capgemini.dllpoc.twilio.delhaize.adapter.in;

import com.capgemini.dllpoc.ai.delhaize.application.CallFlowAgent;
import com.capgemini.dllpoc.ai.delhaize.application.SessionService;
import com.capgemini.dllpoc.twilio.delhaize.adapter.in.dto.TalkRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/twilio")
@CrossOrigin
@Slf4j
public class TwilioController {

    private final CallFlowAgent callFlowAgent;
    private final SessionService sessionService;

    public TwilioController(CallFlowAgent callFlowAgent, SessionService sessionService) {
        this.callFlowAgent = callFlowAgent;
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/process", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> talk(@RequestParam Map<String, String> params) {
        String xml;

        String input = params.getOrDefault("SpeechResult", params.get("Digits"));
        if (input == null) {
            input = "Hey";
        }
        String sessionId = params.get("CallSid");
        TalkRequest request = new TalkRequest(input, sessionId);
        System.out.println(request.input() + " " + request.sessionId());

        if (sessionId != null && !sessionId.trim().isEmpty()) {
            // Use an existing session
            xml = callFlowAgent.talkWithSession(request.sessionId(), request.input());
        } else {
            // Create a new session for this conversation
            sessionId = sessionService.createNewSession();
            xml = callFlowAgent.talkWithSession(sessionId, request.input());
        }
        log.info("Twilio /process response XML BEFORE formatting: {}", xml);
        String formattedXml = formatXML(xml);
        log.info("Twilio /process response XML AFTER formatting: {}", formattedXml);
        return ResponseEntity.ok(formattedXml);
    }

    @PostMapping(value = "/talk", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> talk(@RequestBody TalkRequest request) {
        String xml;
        
        if (request.sessionId() != null && !request.sessionId().trim().isEmpty()) {
            // Use existing session
            xml = callFlowAgent.talkWithSession(request.sessionId(), request.input());
        } else {
            // Create new session for this conversation
            String sessionId = sessionService.createNewSession();
            xml = callFlowAgent.talkWithSession(sessionId, request.input());
        }
        
        return ResponseEntity.ok(xml);
    }

    @DeleteMapping(value = "/session/{sessionId}")
    public ResponseEntity<Void> clearSession(@PathVariable String sessionId) {
        callFlowAgent.clearConversation(sessionId);
        return ResponseEntity.ok().build();
    }

    private String formatXML(String xml) {
        if (xml == null) {
            return null;
        }

        // Remove ```xml at the start (with optional whitespace)
        String result = xml.replaceFirst("(?s)^\\s*```xml\\s*", "");

        // Remove ``` at the end (with optional whitespace)
        result = result.replaceFirst("(?s)\\s*```\\s*$", "");

        return result.trim();
    }
}
