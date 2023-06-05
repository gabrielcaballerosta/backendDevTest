package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

public final class ClientError extends RecommendationError {

    public ClientError(String message, int statusCode) {
        super(message, statusCode);
    }

}
