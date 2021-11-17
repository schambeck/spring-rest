package com.schambeck.rest.controller;

import com.schambeck.rest.domain.Invoice;
import com.schambeck.rest.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/invoices-flux")
@RequiredArgsConstructor
class InvoiceFluxController {

    private final InvoiceService service;

    @GetMapping
    Flux<Invoice> findAll() {
        return Flux.fromIterable(service.findAll());
    }

    @GetMapping("/{id}")
    Flux<Invoice> findById(@PathVariable("id") Long id) {
        return Flux.just(service.findById(id));
    }

    @PostMapping
    Flux<Invoice> create(@RequestBody Invoice invoice) {
        return Flux.just(service.create(invoice));
    }

    @PutMapping("/{id}")
    Flux<Invoice> update(@PathVariable("id") Long id, @RequestBody Invoice invoice) {
        return Flux.just(service.update(id, invoice));
    }

    @DeleteMapping("/{id}")
    Flux<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Flux.empty();
    }

}
