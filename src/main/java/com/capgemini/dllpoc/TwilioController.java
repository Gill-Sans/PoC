package com.capgemini.dllpoc;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Say;
import com.twilio.twiml.voice.Say.Language;
import com.twilio.twiml.voice.Say.Voice;

@RestController
@CrossOrigin
@RequestMapping("/twilio")
public class TwilioController {

	private static final Logger logger = LoggerFactory.getLogger(TwilioController.class);

	private final String accountSid;
	private final String apiKey;
	private final String apiSecret;
	private final String appSid;

	public TwilioController(@Value("${twilio.account.sid}") String accountSid,
			@Value("${twilio.api.key}") String apiKey, @Value("${twilio.api.secret}") String apiSecret,
			@Value("${twilio.app.sid}") String appSid) {

		this.accountSid = accountSid;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.appSid = appSid;
	}

	/** Utility method to create a Say verb with a consistent voice. */
	private Say buildSay(String message) {
		return new Say.Builder(message).voice(Voice.POLLY_LEA).language(Language.FR_FR).build();
	}

	@GetMapping("/token")
	public ResponseEntity<Map<String, String>> generateToken(@RequestParam(defaultValue = "web-user") String identity) {

		if (accountSid == null || apiKey == null || apiSecret == null || appSid == null) {
			logger.error("Twilio credentials are not properly configured.");
			return ResponseEntity.internalServerError().body(Map.of("error", "Twilio configuration missing"));
		}

		VoiceGrant grant = new VoiceGrant();
		grant.setOutgoingApplicationSid(appSid);

        byte[] secretBytes = apiSecret.getBytes(StandardCharsets.UTF_8);
		AccessToken token = new AccessToken.Builder(accountSid, apiKey, secretBytes).identity(identity).grant(grant)
				.build();

		logger.info("Generated Twilio access token for identity: {}", identity);

		return ResponseEntity.ok(Map.of("token", token.toJwt()));
	}

	@PostMapping(value = "/hello", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> handleIncomingCall(@RequestParam("CallSid") String callSid,
			@RequestParam("Caller") String caller) {

		logger.info("Received incoming call: SID={}, Caller={}", callSid, caller);

		VoiceResponse twiml = new VoiceResponse.Builder().say(buildSay(
				"Salut ! Vous êtes en communication avec le Centre de Commande du Service Desk Galactique. Tous nos techniciens sont en orbite autour d’incidents critiques. Transmettez votre message après le bip : nom, unité, et description de l’anomalie. Nous reviendrons vers vous à la vitesse de la lumière !"))
				.hangup(new Hangup.Builder().build()).build();

		return ResponseEntity.ok(twiml.toXml());
	}
}
