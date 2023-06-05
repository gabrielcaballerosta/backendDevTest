package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

public final class ProductNotFound extends RecommendationError {

    public ProductNotFound(String productId) {
        super(String.format("Product [ %s ] not found", productId), 404);
    }

}
