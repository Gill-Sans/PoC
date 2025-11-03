package com.capgemini.dllpoc.twilio.delhaize.adapter.in;

import com.capgemini.dllpoc.ai.delhaize.application.CallFlowAgent;
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

    public TwilioController(CallFlowAgent callFlowAgent) {
        this.callFlowAgent = callFlowAgent;
    }

    @PostMapping(value = "/voice", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> handleIncomingCall(@RequestParam Map<String, String> params) {
        String xml = callFlowAgent.process(params);
        return ResponseEntity.ok(xml);
    }

    @PostMapping(value = "/talk", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> talk(@RequestBody TalkRequest request) {
        String xml = callFlowAgent.talk(request.input());
        return ResponseEntity.ok(xml);
    }
}
