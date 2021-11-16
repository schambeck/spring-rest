package com.schambeck.rest.service;

import com.schambeck.rest.base.exception.ConflictException;
import com.schambeck.rest.base.exception.NotFoundException;
import com.schambeck.rest.domain.Invoice;
import com.schambeck.rest.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

    @Override
    public List<Invoice> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(toList());
    }

    @Override
    public Invoice findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(format("Entity %d not found", id)));
    }

    @Override
    public Invoice create(Invoice invoice) {
        if (invoice.getId() != null && repository.existsById(invoice.getId())) {
            throw new ConflictException(format("Entity %d already exists", invoice.getId()));
        }
        return repository.save(invoice);
    }

    @Override
    public Invoice update(Long id, Invoice invoice) {
        Invoice updated = findById(id);
        updated.setIssued(invoice.getIssued());
        updated.setTotal(invoice.getTotal());
        return repository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new NotFoundException(format("Entity %d not found", id));
        }
    }

}
