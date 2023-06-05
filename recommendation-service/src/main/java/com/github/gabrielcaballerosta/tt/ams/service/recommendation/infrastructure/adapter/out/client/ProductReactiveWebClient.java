package com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.out.client;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.*;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.configuration.ProductDataConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductReactiveWebClient implements DomainProductClient {

    private final Logger logger;

    private final WebClient                webClient;
    private final ProductDataConfiguration productDataConfiguration;

    @Override
    public Flux<String> getSimilarProductsBy(String productId) {
        logger.debug("Retrieving similar product product with id {}", productId);
        return webClient.get()
                .uri("/product/{productId}/similarids", Collections.singletonMap("productId", productId))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new ServerError(String.format("Fail retrieve similar product with id: %s", productId), response.statusCode().value())))
                .bodyToMono(new ParameterizedTypeReference<Set<String>>() {
                })
                .flatMapMany(Flux::fromIterable)
                .retryWhen(Retry
                        .backoff(productDataConfiguration.getMaxAttempts(), Duration.ofSeconds(productDataConfiguration.getBackoffTimeInSecs()))
                        .jitter(0.75)
                        .filter(ServerError.class::isInstance)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServerError("Reached the maximum number of attempts", HttpStatus.SERVICE_UNAVAILABLE.value());
                        }));
    }

    @Override
    @Cacheable("recommendation-service-cache")
    public Mono<ProductDetail> getProductDetail(String productId) {
        logger.debug("Retrieving product with id {}", productId);
        return webClient.get()
                .uri("/product/{productId}", Collections.singletonMap("productId", productId))
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new ServerError(String.format("Fail retrieve product detail with id: %s", productId), response.statusCode().value())))
                .onStatus(statusCode -> statusCode.isSameCodeAs(HttpStatusCode.valueOf(404)), response -> Mono.error(new ProductNotFound(productId)))
                .bodyToMono(ProductDetailMockResponse.class)
                .map(productDetailResponse -> new ProductDetail(
                        new ProductId(productDetailResponse.getId()),
                        new ProductName(productDetailResponse.getName()),
                        new ProductPrice(productDetailResponse.getPrice()),
                        new ProductAvailable(productDetailResponse.isAvailability()))
                ).cache(Duration.ofSeconds(60))
                .retryWhen(Retry
                        .backoff(productDataConfiguration.getMaxAttempts(), Duration.ofSeconds(productDataConfiguration.getBackoffTimeInSecs()))
                        .jitter(0.75)
                        .filter(ServerError.class::isInstance)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServerError("Reached the maximum number of attempts", HttpStatus.SERVICE_UNAVAILABLE.value());
                        }));
    }

}
