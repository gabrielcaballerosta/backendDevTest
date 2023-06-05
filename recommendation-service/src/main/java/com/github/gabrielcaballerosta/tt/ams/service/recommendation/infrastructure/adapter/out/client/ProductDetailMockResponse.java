package com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter.out.client;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductDetailMockResponse implements Serializable {

    private String     id;
    private String     name;
    private BigDecimal price;
    private boolean    availability;

}
