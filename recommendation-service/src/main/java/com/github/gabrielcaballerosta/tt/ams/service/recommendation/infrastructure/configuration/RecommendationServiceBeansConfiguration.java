package com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.configuration;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.Logger;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ServerError;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.LoggerImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.lang.reflect.Field;
import java.time.Duration;

@Configuration
public class RecommendationServiceBeansConfiguration {

    @Bean
    WebClient webClient(WebClient.Builder builder, ProductDataConfiguration productDataConfiguration) {
        HttpClient client = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(productDataConfiguration.getConnectionTimeoutInSecs()));
        return builder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl(productDataConfiguration.getBasePath())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    @Scope("prototype")
    public Logger logging(final InjectionPoint injectionPoint) {
        final Class<?> clazz;
        MethodParameter methodParameter = injectionPoint.getMethodParameter();
        if (methodParameter != null) {
            clazz = methodParameter.getContainingClass();
        } else {
            Field field = injectionPoint.getField();
            clazz = field != null ? field.getDeclaringClass() : null;
        }
        if (clazz == null) {
            throw new ServerError("Technical error :: No class found for logger");
        }
        return new LoggerImpl(LoggerFactory.getLogger(clazz));
    }

}
