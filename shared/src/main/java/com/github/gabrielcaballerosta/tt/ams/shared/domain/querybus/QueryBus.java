package com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus;

public interface QueryBus {

    <R> R handle(Query<R> query);

}
