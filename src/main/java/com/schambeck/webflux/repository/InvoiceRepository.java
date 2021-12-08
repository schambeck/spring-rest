package com.schambeck.webflux.repository;

import com.schambeck.webflux.domain.Invoice;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface InvoiceRepository extends R2dbcRepository<Invoice, Long> {
}
