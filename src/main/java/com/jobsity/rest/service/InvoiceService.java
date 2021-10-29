package com.jobsity.rest.service;

import com.jobsity.rest.base.NotFoundException;
import com.jobsity.rest.domain.Invoice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceService {

    public List<Invoice> findAll() {
        return invoices;
    }

    public Invoice findByIndex(int index) {
        if (notExists(index)) {
            throw new NotFoundException();
        }
        return invoices.get(index);
    }

    public Invoice create(Invoice invoice) {
        return invoice;
    }

    public Invoice update(int index, Invoice invoice) {
        if (notExists(index)) {
            throw new NotFoundException();
        }
        Invoice updated = invoices.get(index);
        updated.setIssued(invoice.getIssued());
        updated.setTotal(invoice.getTotal());
        return updated;
    }

    public void delete(int index) {
        if (notExists(index)) {
            throw new NotFoundException();
        }
        invoices.remove(index);
    }

    private boolean notExists(int index) {
        return index >= invoices.size();
    }

    private static final List<Invoice> invoices = new ArrayList<Invoice>() {{
        add(new Invoice(1L, LocalDate.now(), BigDecimal.valueOf(1000)));
        add(new Invoice(2L, LocalDate.now(), BigDecimal.valueOf(2000)));
        add(new Invoice(3L, LocalDate.now(), BigDecimal.valueOf(3000)));
        add(new Invoice(4L, LocalDate.now(), BigDecimal.valueOf(4000)));
    }};

}
