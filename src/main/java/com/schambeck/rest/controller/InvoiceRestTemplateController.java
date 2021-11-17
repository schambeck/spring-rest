package com.schambeck.rest.controller;

import com.schambeck.rest.domain.Invoice;
import com.schambeck.rest.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices-rest-template")
@RequiredArgsConstructor
class InvoiceRestTemplateController {

    private final InvoiceService service;

    @GetMapping
    List<Invoice> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    Invoice findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping
    Invoice create(@RequestBody Invoice invoice) {
        return service.create(invoice);
    }

    @PutMapping("/{id}")
    Invoice update(@PathVariable("id") Long id, @RequestBody Invoice invoice) {
        return service.update(id, invoice);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
