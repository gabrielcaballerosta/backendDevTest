package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

public final class ServerError extends RecommendationError {

    public ServerError(String message) {
        super(message);
    }

    public ServerError(String message, int statusCode) {
        super(message, statusCode);
    }

}
