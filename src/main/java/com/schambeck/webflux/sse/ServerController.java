package com.schambeck.webflux.sse;

import com.schambeck.webflux.domain.Invoice;
import com.schambeck.webflux.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/stream")
public class ServerController {

    private final InvoiceService service;
    private final FluxEmitter<Invoice> emitter = new FluxEmitter<>();

    @GetMapping(path = "/invoices", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Invoice>> stream() {
        return Flux.create(emitter).map(p -> ServerSentEvent.builder(p)
                .id(UUID.randomUUID().toString())
                .event("message")
                .build());
    }

    @GetMapping(path = "/stream-flux", produces = TEXT_EVENT_STREAM_VALUE)
    public Flux<Invoice> streamFlux() {
        return service.findAll();
    }

    @EventListener
    public void onApplicationEvent(CustomSpringEvent<Invoice> event) {
        emitter.publish(event.getObject());
    }

}
