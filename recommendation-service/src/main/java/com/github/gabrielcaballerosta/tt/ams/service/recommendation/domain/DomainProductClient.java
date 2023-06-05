package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DomainProductClient {

    Flux<String> getSimilarProductsBy(String productId);

    Mono<ProductDetail> getProductDetail(String productId);

}
