package com.schambeck.webflux.controller;

import com.schambeck.webflux.domain.Invoice;
import com.schambeck.webflux.service.InvoiceService;
import com.schambeck.webflux.sse.CustomSpringEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.Duration;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

@Validated
@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
class InvoiceController {

    private final InvoiceService service;
    private final ApplicationEventPublisher publisher;

    @ResponseStatus(OK)
//    @GetMapping(produces = APPLICATION_NDJSON_VALUE)
//    @GetMapping(produces = APPLICATION_STREAM_JSON_VALUE)
    @GetMapping(produces = TEXT_EVENT_STREAM_VALUE)
    Flux<Invoice> findAll() {
        return service.findAll().delayElements(Duration.ofMillis(500));
    }

    @ResponseStatus(OK)
    @GetMapping("/{id}")
    Mono<Invoice> findById(@PathVariable @Positive Long id) {
        return service.findById(id);
    }

    @ResponseStatus(CREATED)
    @PostMapping
    Mono<Invoice> create(@RequestBody @Valid Mono<Invoice> invoice) {
        return service.create(invoice);
    }

    @ResponseStatus(CREATED)
    @PutMapping
    Mono<Invoice> upsert(@RequestBody @Valid Mono<Invoice> invoice) {
        return service.create(invoice);
    }

    @ResponseStatus(OK)
    @PutMapping("/{id}")
    Mono<Invoice> update(@PathVariable @Positive Long id, @RequestBody @Valid Mono<Invoice> invoice) {
        Mono<Invoice> updated = service.update(id, invoice);
        return updated.doOnNext(p -> publisher.publishEvent(new CustomSpringEvent<>(this, p)));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{id}")
    Mono<Void> delete(@PathVariable @Positive Long id) {
        return service.delete(id);
    }

}
