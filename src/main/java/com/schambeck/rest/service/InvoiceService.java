package com.schambeck.rest.service;

import com.schambeck.rest.domain.Invoice;

import java.util.List;

public interface InvoiceService {

    List<Invoice> findAll();

    Invoice findById(Long id);

    Invoice create(Invoice invoice);

    Invoice update(Long id, Invoice invoice);

    void delete(Long id);

}
