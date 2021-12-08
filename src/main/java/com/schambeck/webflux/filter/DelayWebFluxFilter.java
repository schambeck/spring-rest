package com.schambeck.webflux.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static java.time.Duration.ofMillis;

@Component
class DelayWebFluxFilter implements WebFilter {

    @Value("${app.delay.enabled:false}")
    private boolean delayEnabled;

    @Value("${app.delay.duration:500}")
    private int delayDuration;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (delayEnabled) {
            return Mono.delay(ofMillis(delayDuration))
                    .then(chain.filter(exchange));
        }
        return chain.filter(exchange);
    }

}
