
package com.capgemini.dllpoc.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "twilio")
public class TwilioProperties {
    //---------------- Top level Groups ----------------//
    @Valid
    @NotNull
    private Account account;

    @Valid
    @NotNull
    private Api api;

    @Valid
    @NotNull
    private App app;

    //---------------- Nested Groups ----------------//
    @Getter
    @Setter
    public static class Account {
        @NotNull
        private String sid;
    }

    @Getter
    @Setter
    public static class Api {
        @NotNull
        private String key;

        @NotNull
        private String secret;
    }

    @Getter
    @Setter
    public static class App {
        @NotNull
        private String sid;
    }
}