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

    @GetMapping("/{index}")
    public ResponseEntity<Invoice> findByIndex(@PathVariable("index") int index) {
        return ResponseEntity.ok(service.findByIndex(index));
    }

    @PostMapping
    public ResponseEntity<Invoice> create(@RequestBody Invoice invoice) {
        Invoice created = service.create(invoice);
        return ResponseEntity.status(CREATED).body(created);
    }

    @PutMapping("/{index}")
    public ResponseEntity<Invoice> update(@PathVariable("index") int index, @RequestBody Invoice invoice) {
        return ResponseEntity.ok(service.update(index, invoice));
    }

    @DeleteMapping("/{index}")
    public ResponseEntity<Void> delete(@PathVariable("index") int index) {
        service.delete(index);
        return ResponseEntity.status(NO_CONTENT).build();
    }

}
