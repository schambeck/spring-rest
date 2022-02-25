package com.schambeck.webflux.sse;

import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class FluxEmitter<T> implements Consumer<FluxSink<T>> {

    private FluxSink<T> fluxSink;

    @Override
    public void accept(FluxSink<T> fluxSink) {
        this.fluxSink = fluxSink;
    }

    public void publish(T event) {
        this.fluxSink.next(event);
    }

}
