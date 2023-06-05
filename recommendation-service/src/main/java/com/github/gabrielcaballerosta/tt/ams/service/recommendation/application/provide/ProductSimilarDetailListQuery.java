package com.github.gabrielcaballerosta.tt.ams.service.recommendation.application.provide;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ProductDetail;
import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.ProductId;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.Query;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Getter
public class ProductSimilarDetailListQuery extends Query<Flux<ProductDetail>> {

    private final ProductId productId;

}
