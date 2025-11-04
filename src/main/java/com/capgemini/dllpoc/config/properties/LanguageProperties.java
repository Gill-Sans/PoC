package com.capgemini.dllpoc.config.properties;

import com.twilio.twiml.voice.Say;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.languages")
public class LanguageProperties {

    /**
     * List of required language codes.
     */
    private List<Say.Language> required = Collections.emptyList();

    /**
     * List for language selection options
     * Map of digit -> (language code -> selection prompt message)
     */
    private Map<Integer, Map<Say.Language, String>> selection = new LinkedHashMap<>();

    /**
     * List for timeout messages
     * Map of language code -> timeout message
     */
    private Map<Say.Language, String> timeout = new LinkedHashMap<>();

    /**
     * Prompts for asking user's name
     * Map of language code -> name prompt message
     */
    private Map<Say.Language, String> askName = new LinkedHashMap<>();

    /**
     * Prompts for asking user's account number
     * Map of language code -> account prompt message
     */
    private Map<Say.Language, String> askAccount = new LinkedHashMap<>();

    /**
     * Template for confirming user details (problem description)
     * Map of language code -> confirmation template
     * Template should contain placeholders: %s for name, %s for account, %s for problem
     */
    private Map<Say.Language, String> confirmProblem = new LinkedHashMap<>();

    /**
     * Thank you messages when ending the call
     * Map of language code -> thank you template
     * Template should contain placeholder: %s for name
     */
    private Map<Say.Language, String> thankYou = new LinkedHashMap<>();

    /**
     * Retry messages when user needs to provide information again
     * Map of language code -> retry message
     */
    private Map<Say.Language, String> retry = new LinkedHashMap<>();

    /**
     * Validates that required languages are present in all language property maps.
     * @throws IllegalStateException if any required language is missing
     */
    @PostConstruct
    void validateRequiredLanguages() {
        if (required == null || required.isEmpty()) {
            return;
        }

        // Validate selection (nested map: digit -> { languageCode -> message })
        var allSelectionLanguages = selection.values().stream()
                .flatMap(languageMap -> languageMap.keySet().stream())
                .collect(Collectors.toSet());
        var missingInSelection = required.stream()
                .filter(lang -> !allSelectionLanguages.contains(lang))
                .toList();
        if (!missingInSelection.isEmpty()) {
            throw new IllegalStateException(
                    "Missing required languages " + missingInSelection + " in app.languages.selection"
            );
        }

        validateMap("timeout", timeout);

        // Validate askName
        validateMap("askName", askName);

        // Validate askAccount
        validateMap("askAccount", askAccount);

        // Validate confirmProblem
        validateMap("confirmProblem", confirmProblem);

        // Validate thankYou
        validateMap("thankYou", thankYou);

        // Validate retry
        validateMap("retry", retry);
    }

    private void validateMap(String mapName, Map<Say.Language, String> map) {
        var missing = required.stream()
                .filter(lang -> !map.containsKey(lang))
                .toList();
        if (!missing.isEmpty()) {
            throw new IllegalStateException(
                    "Missing required languages " + missing + " in app.languages." + mapName
            );
        }
    }
}
