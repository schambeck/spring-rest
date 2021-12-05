package com.schambeck.rest.repository;

import com.schambeck.rest.domain.Invoice;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface InvoiceRepository extends R2dbcRepository<Invoice, Long> {
}
