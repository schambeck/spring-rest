package com.jobsity.rest.controller;

import com.jobsity.rest.domain.Invoice;
import com.jobsity.rest.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Invoice> create(@RequestBody Invoice invoice) {
        Invoice created = service.create(invoice);
        return ResponseEntity.status(CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> update(@PathVariable("id") Long id, @RequestBody Invoice invoice) {
        return ResponseEntity.ok(service.update(id, invoice));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
