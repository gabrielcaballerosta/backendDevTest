package com.github.gabrielcaballerosta.tt.ams.service.recommendation.provide;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.application.provide.ProductSimilarDetailListQuery;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.application.provide.ProductSimilarDetailListQueryHandler;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.*;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.QueryHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

class ProductSimilarDetailListQueryHandlerTest {

    private final DomainProductClient domainProductClient;

    private final QueryHandler<Flux<ProductDetail>, ProductSimilarDetailListQuery> handler;

    private ProductSimilarDetailListQueryHandlerTest() {
        Logger logger = Mockito.mock(Logger.class);
        this.domainProductClient = Mockito.mock(DomainProductClient.class);
        this.handler = new ProductSimilarDetailListQueryHandler(logger, domainProductClient);
    }

    @Test
    void whenExistSimilarProductGivenProductIdAndReturnOne_shouldReturnFluxOneElement() {
        String id = UUID.randomUUID().toString();
        ProductSimilarDetailListQuery query = new ProductSimilarDetailListQuery(new ProductId(id));
        ProductDetail productDetail = ProductDetailMother.random();

        when(domainProductClient.getSimilarProductsBy(Mockito.anyString())).thenReturn(Flux.just("1"));
        when(domainProductClient.getProductDetail(Mockito.anyString())).thenReturn(Mono.just(productDetail));

        Flux<ProductDetail> similarProductDetailsResponse = handler.handle(query);
        StepVerifier.create(similarProductDetailsResponse)
                .expectNext(productDetail)
                .expectComplete()
                .verify();
    }

    @Test
    void whenExistSimilarProductGivenProductIdAndReturnTwo_shouldReturnFluxTwoElement() {
        String id = UUID.randomUUID().toString();
        ProductSimilarDetailListQuery query = new ProductSimilarDetailListQuery(new ProductId(id));
        List<ProductDetail> productDetails = ProductDetailMother.randomList(2);

        when(domainProductClient.getSimilarProductsBy(Mockito.anyString())).thenReturn(Flux.just("1", "2"));
        when(domainProductClient.getProductDetail(Mockito.anyString()))
                .thenReturn(Mono.just(productDetails.get(0)))
                .thenReturn(Mono.just(productDetails.get(1)));

        Flux<ProductDetail> similarProductDetailsResponse = handler.handle(query);
        StepVerifier.create(similarProductDetailsResponse)
                .expectNext(productDetails.get(0))
                .expectNext(productDetails.get(1))
                .expectComplete()
                .verify();
    }

    @Test
    void whenFirstElementOkAndSecondKo_shouldReturnFluxOneElement() {
        String id = UUID.randomUUID().toString();
        ProductSimilarDetailListQuery query = new ProductSimilarDetailListQuery(new ProductId(id));
        ProductDetail productDetail = ProductDetailMother.random();

        when(domainProductClient.getSimilarProductsBy(Mockito.anyString())).thenReturn(Flux.just("1", "2"));
        when(domainProductClient.getProductDetail(Mockito.anyString()))
                .thenReturn(Mono.just(productDetail))
                .thenReturn(Mono.error(new ServerError("ignore")));

        Flux<ProductDetail> similarProductDetailsResponse = handler.handle(query);
        StepVerifier.create(similarProductDetailsResponse)
                .expectNext(productDetail)
                .expectComplete()
                .verify();
    }

    @Test
    void whenFirstElementKoAndSecondOk_shouldReturnFluxOneElement() {
        String id = UUID.randomUUID().toString();
        ProductSimilarDetailListQuery query = new ProductSimilarDetailListQuery(new ProductId(id));
        ProductDetail productDetail = ProductDetailMother.random();

        when(domainProductClient.getSimilarProductsBy(Mockito.anyString())).thenReturn(Flux.just("1", "2"));
        when(domainProductClient.getProductDetail(Mockito.anyString()))
                .thenReturn(Mono.error(new ServerError("ignore")))
                .thenReturn(Mono.just(productDetail));

        Flux<ProductDetail> similarProductDetailsResponse = handler.handle(query);
        StepVerifier.create(similarProductDetailsResponse)
                .expectNext(productDetail)
                .expectComplete()
                .verify();
    }

    @Test
    void whenNoSimilarProductsExistForGivenProduct_shouldFluxEmpty() {
        String id = UUID.randomUUID().toString();
        ProductSimilarDetailListQuery query = new ProductSimilarDetailListQuery(new ProductId(id));

        when(domainProductClient.getSimilarProductsBy(Mockito.anyString())).thenReturn(Flux.empty());

        Flux<ProductDetail> similarProductDetailsResponse = handler.handle(query);
        StepVerifier.create(similarProductDetailsResponse)
                .expectComplete()
                .verify();
    }

    @Test
    void whenError_shouldBeCatchMonoError() {
        String id = UUID.randomUUID().toString();
        ProductSimilarDetailListQuery query = new ProductSimilarDetailListQuery(new ProductId(id));

        when(domainProductClient.getSimilarProductsBy(Mockito.anyString())).thenReturn(Flux.error(new ServerError("Fail request")));

        Flux<ProductDetail> similarProductDetailsResponse = handler.handle(query);
        StepVerifier.create(similarProductDetailsResponse)
                .expectErrorMatches(throwable -> throwable instanceof ServerError && "Fail request".equals(throwable.getMessage()))
                .verify();
    }

}
