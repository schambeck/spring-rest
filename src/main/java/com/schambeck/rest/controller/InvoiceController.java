package com.schambeck.rest.controller;

import com.schambeck.rest.domain.Invoice;
import com.schambeck.rest.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Validated
@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
class InvoiceController {

    private final InvoiceService service;

    @GetMapping
    Mono<ResponseEntity<List<Invoice>>> findAll() {
        return Mono.just(service.findAll()).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    Mono<ResponseEntity<Invoice>> findById(@PathVariable("id") @Positive Long id) {
        return Mono.just(service.findById(id)).map(ResponseEntity::ok);
    }

    @PostMapping
    Mono<ResponseEntity<Invoice>> create(@RequestBody @Valid Invoice invoice) {
        Invoice created = service.create(invoice);
        return Mono.just(created).map(p -> ResponseEntity.status(CREATED).body(created));
    }

    @PutMapping("/{id}")
    Mono<ResponseEntity<Invoice>> update(@PathVariable("id") @Positive Long id, @RequestBody @Valid Invoice invoice) {
        return Mono.just(service.update(id, invoice)).map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> delete(@PathVariable("id") @Positive Long id) {
        service.delete(id);
        return Mono.just(ResponseEntity.status(NO_CONTENT).build());
    }

}
