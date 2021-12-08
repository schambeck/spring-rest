package com.schambeck.webflux.service;

import com.schambeck.webflux.base.exception.NotFoundException;
import com.schambeck.webflux.domain.Invoice;
import com.schambeck.webflux.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

    @Override
    public Flux<Invoice> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Invoice> findById(Long id) {
        return repository.findById(id).switchIfEmpty(Mono.error(new NotFoundException(format("Entity %d not found", id))));
    }

    @Override
    public Mono<Invoice> create(Mono<Invoice> invoice) {
        return invoice.flatMap(p -> repository.save(p.setAsNew())
                .onErrorResume(DataIntegrityViolationException.class, e -> Mono.error(new DataIntegrityViolationException(format("Entity %d already exists", p.getId())))));
    }

    @Override
    public Mono<Invoice> update(Long id, Mono<Invoice> invoice) {
        return invoice.flatMap(p -> Mono.just(p).zipWith(findById(id))
                .flatMap(q -> repository.save(q.getT2().toBuilder().issued(q.getT1().getIssued()).total(q.getT1().getTotal()).build())));
    }

    @Override
    public Mono<Void> delete(Long id) {
        return repository.existsById(id)
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new NotFoundException(format("Entity %d not found", id))))
                .flatMap(exists -> repository.deleteById(id));
    }

}
