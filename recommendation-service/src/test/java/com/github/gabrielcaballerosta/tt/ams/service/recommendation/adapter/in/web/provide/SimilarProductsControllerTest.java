package com.github.gabrielcaballerosta.tt.ams.service.recommendation.adapter.in.web.provide;

import com.github.gabrielcaballerosta.tt.ams.infrastructure.adapter.ProductDetailServiceResponse;
import com.github.gabrielcaballerosta.tt.ams.infrastructure.adapter.in.web.ProductApi;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.Logger;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ProductDetail;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ProductDetailMother;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.in.web.provide.SimilarProductsController;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.Query;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.QueryBus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimilarProductsControllerTest {

    private static final String SIMILAR_PRODUCT_ID_URL = "/product/{productId}/similar";

    private final QueryBus queryBus;

    private final WebTestClient webClient;

    private SimilarProductsControllerTest() {
        Logger logger = Mockito.mock(Logger.class);
        queryBus = Mockito.mock(QueryBus.class);
        ProductApi productApi = new SimilarProductsController(logger, queryBus);
        webClient = WebTestClient.bindToController(productApi).build();
    }

    @Test
    void whenQueryBusReturnNoElements_shouldReturnEmptyFlux() {
        when(queryBus.handle(Mockito.any(Query.class))).thenReturn(Flux.empty());

        webClient
                .get().uri(SIMILAR_PRODUCT_ID_URL, "1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ProductDetailServiceResponse.class)
                .hasSize(0);
    }

    @Test
    void whenQueryBusReturnElements_shouldReturnTheseElements() {
        ProductDetail productDetail = ProductDetailMother.random();

        when(queryBus.handle(Mockito.any(Query.class))).thenReturn(Flux.just(productDetail));

        webClient
                .get().uri(SIMILAR_PRODUCT_ID_URL, "1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(ProductDetailServiceResponse.class)
                .hasSize(1)
                .value(productDetailServiceResponses -> {
                    List<ProductDetailServiceResponse> responses = productDetailServiceResponses.stream().toList();
                    ProductDetailServiceResponse productDetailServiceResponse = responses.get(0);
                    assertThat(productDetailServiceResponse.getAvailability()).isEqualTo(productDetail.availability().value());
                    assertThat(productDetailServiceResponse.getId()).isEqualTo(productDetail.id().value());
                    assertThat(productDetailServiceResponse.getName()).isEqualTo(productDetail.name().value());
                    assertThat(productDetailServiceResponse.getPrice()).isEqualTo(productDetail.price().value());
                });

        verify(queryBus).handle(Mockito.any(Query.class));
    }


}
