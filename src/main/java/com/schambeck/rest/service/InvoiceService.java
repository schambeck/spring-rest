package com.schambeck.rest.service;

import com.schambeck.rest.domain.Invoice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvoiceService {

    Flux<Invoice> findAll();

    Mono<Invoice> findById(Long id);

    Mono<Invoice> create(Mono<Invoice> invoice);

    Mono<Invoice> update(Long id, Mono<Invoice> invoice);

    Mono<Void> delete(Long id);

}
