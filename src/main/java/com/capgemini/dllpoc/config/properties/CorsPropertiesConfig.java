package com.capgemini.dllpoc.config.properties;

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
@ConfigurationProperties(prefix = "cors")
public class CorsPropertiesConfig {
    @NotNull
    private String[] allowedOrigins;

    @NotNull
    private String[] allowedMethods;

    @NotNull
    private String[] allowedHeaders;
}
