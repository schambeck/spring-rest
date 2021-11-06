package com.jobsity.rest.service;

import com.jobsity.rest.base.ConflictException;
import com.jobsity.rest.base.NotFoundException;
import com.jobsity.rest.domain.Invoice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public List<Invoice> findAll() {
        return invoices;
    }

    @Override
    public Invoice findById(Long id) {
        return invoices.stream()
                .filter(invoice -> invoice.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(format("Entity %d not found", id)));
    }

    @Override
    public Invoice create(Invoice invoice) {
        try {
            findById(invoice.getId());
            throw new ConflictException(format("Entity %d already exists", invoice.getId()));
        } catch (NotFoundException e) {
            Invoice created = new Invoice(invoice.getId(), invoice.getIssued(), invoice.getTotal());
            invoices.add(created);
            return created;
        }
    }

    @Override
    public Invoice update(Long id, Invoice invoice) {
        Invoice updated = findById(id);
        updated.setIssued(invoice.getIssued());
        updated.setTotal(invoice.getTotal());
        return updated;
    }

    @Override
    public void delete(Long id) {
        Invoice invoice = findById(id);
        invoices.remove(invoice);
    }

    private static final List<Invoice> invoices = new ArrayList<Invoice>() {{
        add(createInvoice(1L, "2021-02-01", 1000));
        add(createInvoice(2L, "2021-02-02", 2000));
        add(createInvoice(3L, "2021-02-03", 3000));
        add(createInvoice(4L, "2021-02-04", 4000));
    }};

    private static Invoice createInvoice(Long id, String issued, double total) {
        return new Invoice(id, LocalDate.parse(issued), BigDecimal.valueOf(total));
    }

}
