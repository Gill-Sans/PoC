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
     * Validates that required languages are present in all language property maps.
     * @throws IllegalStateException if any required language is missing
     */
    @PostConstruct
    void validateRequiredLanguages() {
        if (required == null || required.isEmpty()) {
            return;
        }

        // Validate timeout (flat map: languageCode -> message)
        var missingInTimeout = required.stream()
                .filter(lang -> !timeout.containsKey(lang))
                .toList();
        if (!missingInTimeout.isEmpty()) {
            throw new IllegalStateException(
                    "Missing required languages " + missingInTimeout + " in app.languages.timeout"
            );
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
    }
}
