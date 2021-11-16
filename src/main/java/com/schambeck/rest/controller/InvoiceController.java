package com.schambeck.rest.controller;

import com.schambeck.rest.domain.Invoice;
import com.schambeck.rest.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Validated
@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
class InvoiceController {

    private final InvoiceService service;

    @GetMapping
    ResponseEntity<List<Invoice>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Invoice> findById(@PathVariable("id") @Positive Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    ResponseEntity<Invoice> create(@RequestBody @Valid Invoice invoice) {
        Invoice created = service.create(invoice);
        return ResponseEntity.status(CREATED).body(created);
    }

    @PutMapping("/{id}")
    ResponseEntity<Invoice> update(@PathVariable("id") @Positive Long id, @RequestBody @Valid Invoice invoice) {
        return ResponseEntity.ok(service.update(id, invoice));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") @Positive Long id) {
        service.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
