package com.capgemini.dllpoc.twilio.delhaize.adapter.in;

import com.capgemini.dllpoc.ai.delhaize.application.CallFlowAgent;
import com.capgemini.dllpoc.ai.delhaize.application.SessionService;
import com.capgemini.dllpoc.twilio.delhaize.adapter.in.dto.TalkRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/twilio")
@CrossOrigin
public class TwilioController {

    private final CallFlowAgent callFlowAgent;
    private final SessionService sessionService;

    public TwilioController(CallFlowAgent callFlowAgent, SessionService sessionService) {
        this.callFlowAgent = callFlowAgent;
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/process", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> handleIncomingCall(@RequestParam Map<String, String> params) {
        String xml = callFlowAgent.process(params);
        return ResponseEntity.ok(xml);
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
}
