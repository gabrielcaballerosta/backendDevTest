package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

public record ProductName(String value) {

    public ProductName {
        if (value == null || value.trim().isEmpty()) {
            throw new ClientError(String.format("Product name is not valid: [%s]", value), 400);
        }
    }

}
