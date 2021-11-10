package com.jobsity.rest.repository;

import com.jobsity.rest.domain.Invoice;

import java.util.List;

public interface InvoiceRepository {

    List<Invoice> findAll();

    Invoice findById(Long id);

    Invoice create(Invoice invoice);

    Invoice update(Long id, Invoice invoice);

    void delete(Long id);

}
