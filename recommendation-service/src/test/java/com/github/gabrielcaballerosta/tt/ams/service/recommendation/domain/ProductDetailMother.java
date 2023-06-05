package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.out.client.ProductDetailMockResponse;
import org.jeasy.random.EasyRandom;

import java.util.List;

public class ProductDetailMother {

    public static ProductDetail random() {
        ProductDetailMockResponse productDetailMockResponse = new EasyRandom().nextObject(ProductDetailMockResponse.class);
        return from(productDetailMockResponse);
    }

    public static List<ProductDetail> randomList(int expectedSize) {
        return new EasyRandom().objects(ProductDetailMockResponse.class, expectedSize)
                .map(ProductDetailMother::from)
                .toList();
    }

    private static ProductDetail from(ProductDetailMockResponse productDetailMockResponse) {
        return new ProductDetail(
                new ProductId(productDetailMockResponse.getId()),
                new ProductName(productDetailMockResponse.getName()),
                new ProductPrice(productDetailMockResponse.getPrice()),
                new ProductAvailable(productDetailMockResponse.isAvailability())
        );
    }

}
