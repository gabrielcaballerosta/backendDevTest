package com.github.gabrielcaballerosta.tt.ams.service.recommendation.application.provide;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.DomainProductClient;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.Logger;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ProductDetail;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ProductId;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class ProductSimilarDetailListQueryHandler implements QueryHandler<Flux<ProductDetail>, ProductSimilarDetailListQuery> {

    private final Logger logger;

    private final DomainProductClient productClient;

    @Override
    public Flux<ProductDetail> handle(ProductSimilarDetailListQuery query) {
        logger.debug("Querying similar products from query handler: {}", getClass().getSimpleName());
        ProductId productId = query.getProductId();
        Flux<String> similarProductIds = productClient.getSimilarProductsBy(productId.value());
        return similarProductIds
                .flatMapSequential(productClient::getProductDetail)
                .onErrorContinue((throwable, object) -> logger.error(throwable.getMessage(), throwable));
    }
}
