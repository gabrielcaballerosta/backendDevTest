package com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.in.web.provide;

import com.github.gabrielcaballerosta.tt.ams.infrastructure.adapter.ProductDetailServiceResponse;
import com.github.gabrielcaballerosta.tt.ams.infrastructure.adapter.in.web.ProductApi;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.application.provide.ProductSimilarDetailListQuery;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.Logger;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ProductDetail;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ProductId;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.Query;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.QueryBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Controller
@RequiredArgsConstructor
public class SimilarProductsController implements ProductApi {

    private final Logger logger;

    private final QueryBus queryBus;

    @Override
    public Mono<ResponseEntity<Flux<ProductDetailServiceResponse>>> getSimilarProducts(String productId, ServerWebExchange exchange) {
        logger.debug("Received new request to obtain similar products for the product id: {}", productId);
        Query<Flux<ProductDetail>> query = new ProductSimilarDetailListQuery(new ProductId(productId));
        Flux<ProductDetailServiceResponse> similarProductsResponse = queryBus.handle(query).map(productDetailToApiResponse());
        return Mono.just(ResponseEntity.ok(similarProductsResponse));
    }

    private Function<ProductDetail, ProductDetailServiceResponse> productDetailToApiResponse() {
        return productDetail -> ProductDetailServiceResponse.builder()
                .id(productDetail.id().value())
                .name(productDetail.name().value())
                .price(productDetail.price().value())
                .availability(productDetail.availability().value())
                .build();
    }

}
