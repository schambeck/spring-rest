package com.schambeck.webflux.sse;

import org.springframework.context.ApplicationEvent;

public class CustomSpringEvent<T> extends ApplicationEvent {
    
    private final T object;

    public CustomSpringEvent(Object source, T object) {
        super(source);
        this.object = object;
    }

    public T getObject() {
        return object;
    }
    
}