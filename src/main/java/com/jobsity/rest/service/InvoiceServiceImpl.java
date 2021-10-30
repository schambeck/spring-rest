package com.jobsity.rest.service;

import com.jobsity.rest.base.ConflictException;
import com.jobsity.rest.base.NotFoundException;
import com.jobsity.rest.domain.Invoice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.Month.FEBRUARY;

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
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Invoice create(Invoice invoice) {
        try {
            findById(invoice.getId());
            throw new ConflictException();
        } catch (NotFoundException e) {
            invoices.add(invoice);
            return invoice;
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
        add(new Invoice(1L, LocalDate.of(2021, FEBRUARY, 1), BigDecimal.valueOf(1000)));
        add(new Invoice(2L, LocalDate.of(2021, FEBRUARY, 2), BigDecimal.valueOf(2000)));
        add(new Invoice(3L, LocalDate.of(2021, FEBRUARY, 3), BigDecimal.valueOf(3000)));
        add(new Invoice(4L, LocalDate.of(2021, FEBRUARY, 4), BigDecimal.valueOf(4000)));
    }};

}
