package com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix = "api.products")
public class ProductDataConfiguration {

    private String basePath;
    private int    connectionTimeoutInSecs;
    private int    maxAttempts;
    private int    backoffTimeInSecs;

}
