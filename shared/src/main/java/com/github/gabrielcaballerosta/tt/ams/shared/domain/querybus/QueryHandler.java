package com.github.gabrielcaballerosta.tt.ams.shared.domain.querybus;

public interface QueryHandler<R, Q extends Query<R>> {

    R handle(Q query);

}
