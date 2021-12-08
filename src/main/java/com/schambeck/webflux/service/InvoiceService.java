package com.schambeck.webflux.service;

import com.schambeck.webflux.domain.Invoice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvoiceService {

    Flux<Invoice> findAll();

    Mono<Invoice> findById(Long id);

    Mono<Invoice> create(Mono<Invoice> invoice);

    Mono<Invoice> update(Long id, Mono<Invoice> invoice);

    Mono<Void> delete(Long id);

}
