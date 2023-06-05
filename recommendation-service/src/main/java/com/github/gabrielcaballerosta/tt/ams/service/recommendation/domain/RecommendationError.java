package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
@Getter
public class RecommendationError extends RuntimeException {

    private final int statusCode;

    protected RecommendationError(String message) {
        super(message);
        this.statusCode = 500;
    }

    protected RecommendationError(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
