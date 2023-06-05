package com.github.gabrielcaballerosta.tt.ams.service.recommendation.adapter.out.client;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.out.client.ProductDetailMockResponse;
import org.jeasy.random.EasyRandom;

public class ProductDetailMockResponseMother {

    public static ProductDetailMockResponse random() {
        return new EasyRandom().nextObject(ProductDetailMockResponse.class);
    }

}
