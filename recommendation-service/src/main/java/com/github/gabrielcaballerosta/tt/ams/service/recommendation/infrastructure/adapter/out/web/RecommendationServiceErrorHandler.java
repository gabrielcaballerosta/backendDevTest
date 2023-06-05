package com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.out.web;

import com.github.gabrielcaballerosta.tt.ams.infrastructure.adapter.ErrorResponse;
import com.github.gabrielcaballerosta.tt.ams.infrastructure.adapter.ServiceResponse;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.Logger;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.RecommendationError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;

@ControllerAdvice
@RequiredArgsConstructor
public class RecommendationServiceErrorHandler {

    private final Logger logger;

    @ExceptionHandler
    public Mono<ResponseEntity<ServiceResponse>> handle(RecommendationError recommendationError) {
        logger.error(recommendationError.getMessage(), recommendationError);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(recommendationError.getMessage())
                .statusCode(recommendationError.getStatusCode())
                .build();
        ServiceResponse serviceResponse = ServiceResponse.builder()
                .errors(Collections.singletonList(errorResponse))
                .success(false)
                .build();
        return Mono.just(new ResponseEntity<>(serviceResponse, HttpStatus.valueOf(recommendationError.getStatusCode())));
    }

    @ExceptionHandler
    public Mono<ResponseEntity<ServiceResponse>> handle(WebClientResponseException webClientRequestException) {
        logger.error(webClientRequestException.getMessage(), webClientRequestException);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(webClientRequestException.getMessage())
                .statusCode(webClientRequestException.getStatusCode().value())
                .build();
        ServiceResponse serviceResponse = ServiceResponse.builder()
                .errors(Collections.singletonList(errorResponse))
                .success(false)
                .build();
        return Mono.just(new ResponseEntity<>(serviceResponse, HttpStatus.valueOf(webClientRequestException.getStatusCode().value())));
    }

}
