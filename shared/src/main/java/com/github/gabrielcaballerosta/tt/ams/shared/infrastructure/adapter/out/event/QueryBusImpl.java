package com.github.gabrielcaballerosta.tt.ams.shared.infrastructure.adapter.out.event;

import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.Query;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.QueryBus;
import com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus.QueryHandler;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QueryBusImpl implements QueryBus {

    private final Map<Class, QueryHandler> handlers;

    public QueryBusImpl(List<QueryHandler> queryHandlerImplementations) {
        this.handlers = new HashMap<>();
        queryHandlerImplementations.forEach(queryHandler -> {
            Class queryClass = getQueryClass(queryHandler);
            handlers.put(queryClass, queryHandler);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T handle(Query<T> query) {
        if (!handlers.containsKey(query.getClass())) {
            throw new RuntimeException(String.format("Technical error :: No found handler for %s", query.getClass().getName()));
        }
        return (T) handlers.get(query.getClass()).handle(query);
    }

    private Class<?> getQueryClass(QueryHandler handler) {
        return GenericTypeResolver.resolveTypeArguments(handler.getClass(), QueryHandler.class)[1];
    }

}
