package com.github.gabrielcaballerosta.tt.ams.service.recommendation.adapter.out.client;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.*;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.out.client.ProductDetailMockResponse;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.out.client.ProductReactiveWebClient;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.configuration.ProductDataConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.when;

class ProductReactiveWebClientTest {

    private final WebClient webClient;

    private final DomainProductClient productReactiveWebClient;

    private ProductReactiveWebClientTest() {
        Logger logger = Mockito.mock(Logger.class);
        this.webClient = Mockito.mock(WebClient.class);
        ProductDataConfiguration productDataConfiguration = new ProductDataConfiguration();
        this.productReactiveWebClient = new ProductReactiveWebClient(logger, webClient, productDataConfiguration);
    }

    @Test
    void givenProductId_whenGetOneSimilarProduct_shouldReturnOneElement() {
        String id = UUID.randomUUID().toString();
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(Mockito.anyString(), Mockito.anyMap())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(Mockito.any(), Mockito.any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Mockito.any(ParameterizedTypeReference.class))).thenReturn(Mono.just(Set.of("1")));

        Flux<String> similarProducts = productReactiveWebClient.getSimilarProductsBy(id);
        StepVerifier.create(similarProducts)
                .expectNext("1")
                .expectComplete()
                .verify();
    }

    @Test
    void givenProductId_whenExistProductDetailNoExist_shouldReturnMonoEmpty() {
        String id = UUID.randomUUID().toString();

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(Mockito.anyString(), Mockito.anyMap())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(Mockito.any(), Mockito.any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Mockito.any(Class.class))).thenReturn(Mono.empty());

        Mono<ProductDetail> similarProducts = productReactiveWebClient.getProductDetail(id);
        StepVerifier.create(similarProducts)
                .expectComplete()
                .verify();
    }

    @Test
    void givenProductId_whenExistProductDetail_shouldReturnProductDetail() {
        String id = UUID.randomUUID().toString();
        ProductDetailMockResponse productDetailMockResponse = ProductDetailMockResponseMother.random();
        ProductDetail productDetail = new ProductDetail(new ProductId(productDetailMockResponse.getId()), new ProductName(productDetailMockResponse.getName()),
                new ProductPrice(productDetailMockResponse.getPrice()), new ProductAvailable(productDetailMockResponse.isAvailability()));

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(Mockito.anyString(), Mockito.anyMap())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(Mockito.any(), Mockito.any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Mockito.any(Class.class))).thenReturn(Mono.just(productDetailMockResponse));

        Mono<ProductDetail> similarProducts = productReactiveWebClient.getProductDetail(id);
        StepVerifier.create(similarProducts)
                .expectNext(productDetail)
                .expectComplete()
                .verify();
    }

}
