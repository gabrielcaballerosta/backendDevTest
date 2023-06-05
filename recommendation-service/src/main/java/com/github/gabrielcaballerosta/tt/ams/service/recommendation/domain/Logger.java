package com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain;

public interface Logger {

    void debug(String message, Object... o);

    void error(String message, Throwable e);

}
