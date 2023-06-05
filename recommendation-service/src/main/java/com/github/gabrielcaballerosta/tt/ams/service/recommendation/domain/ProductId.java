package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

public record ProductId(String value) {

    public ProductId {
        if (value == null || value.trim().isEmpty()) {
            throw new ClientError(String.format("Product [ %s ] not valid: ", value), 400);
        }
    }

}

