package com.github.gabrielcaballerosta.tt.ams.service.recommendation.infrastructure.adapter;

import com.github.gabrielcaballerosta.tt.ams.service.recommendation.domain.Logger;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoggerImpl implements Logger {

    private final org.slf4j.Logger logger;

    @Override
    public void debug(String message, Object... o) {
        logger.debug(message, o);
    }

    @Override
    public void error(String message, Throwable e) {
        logger.error(message, e);
    }
}
