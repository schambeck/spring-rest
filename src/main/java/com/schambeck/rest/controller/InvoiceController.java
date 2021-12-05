package com.schambeck.rest.controller;

import com.schambeck.rest.domain.Invoice;
import com.schambeck.rest.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE;

@Validated
@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
class InvoiceController {

    private final InvoiceService service;

    @ResponseStatus(OK)
    @GetMapping(produces = APPLICATION_NDJSON_VALUE)
    Flux<Invoice> findAll() {
        return service.findAll();
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
        return service.update(id, invoice);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{id}")
    Mono<Void> delete(@PathVariable @Positive Long id) {
        return service.delete(id);
    }

}
