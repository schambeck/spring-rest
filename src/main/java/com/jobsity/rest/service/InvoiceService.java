package com.jobsity.rest.service;

import com.jobsity.rest.domain.Invoice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class InvoiceService {

    private static final List<Invoice> invoices = new ArrayList<>(Arrays.asList(
            new Invoice(1L, LocalDate.now(), BigDecimal.valueOf(1000)),
            new Invoice(2L, LocalDate.now(), BigDecimal.valueOf(2000))
    ));

    public List<Invoice> findAll() {
        return invoices;
    }

    public Invoice findByIndex(int index) {
        return invoices.get(index);
    }

    public Invoice create(Invoice invoice) {
        return invoice;
    }

    public Invoice update(int index, Invoice invoice) {
        Invoice updated = invoices.get(index);
        updated.setIssued(invoice.getIssued());
        updated.setTotal(invoice.getTotal());
        return updated;
    }

    public void delete(int index) {
        invoices.remove(index);
    }

    public boolean exists(int index) {
        return index < invoices.size();
    }

}
