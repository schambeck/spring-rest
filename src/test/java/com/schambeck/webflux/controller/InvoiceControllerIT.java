package com.schambeck.webflux.controller;

import com.schambeck.webflux.exception.NotFoundException;
import com.schambeck.webflux.domain.Invoice;
import com.schambeck.webflux.exception.ValidationErrorResponse;
import com.schambeck.webflux.exception.Violation;
import com.schambeck.webflux.repository.InvoiceRepository;
import com.schambeck.webflux.service.InvoiceServiceImpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Tag("integration")
@Import(InvoiceServiceImpl.class)
@WebFluxTest(InvoiceController.class)
class InvoiceControllerIT {

	@Autowired
	private WebTestClient webClient;

	@MockBean
	private InvoiceRepository repository;

	private static Invoice createInvoice(Long id, String issued, double total) {
		return new Invoice(id, LocalDate.parse(issued), BigDecimal.valueOf(total));
	}

	private static Mono<Invoice> createInvoiceMono(Long id, String issued, double total) {
		return Mono.just(createInvoice(id, issued, total));
	}

	private static void assertViolation(Violation violation) {
		assertEquals("InvoiceController.findById.id", violation.getField());
		assertEquals("deve ser maior que 0", violation.getMessage());
		assertEquals("0", violation.getValue());
	}

	private void assertInvoice(List<Invoice> invoices, int index, int id, String issued, double total) {
		Invoice invoice = invoices.get(index);
		assertInvoice(invoice, id, issued, total);
	}

	private void assertInvoice(Invoice invoice, Integer id, String issued, double total) {
		assertEquals(id.longValue(), invoice.getId());
		assertEquals(LocalDate.parse(issued), invoice.getIssued());
		assertEquals(total, invoice.getTotal().doubleValue());
	}

	@Test
	void findById() {
		when(repository.findById(1L)).thenReturn(createInvoiceMono(1L, "2021-02-01", 1000));

		webClient.get()
				.uri("/invoices/{id}", 1)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Invoice.class)
				.value(invoice -> assertInvoice(invoice, 1, "2021-02-01", 1000D));
	}

	@Test
	void findAll() {
		List<Invoice> mockInvoices = new ArrayList<>() {{
			add(createInvoice(1L, "2021-02-01", 1000));
			add(createInvoice(2L, "2021-02-02", 2000));
			add(createInvoice(3L, "2021-02-03", 3000));
			add(createInvoice(4L, "2021-02-04", 4000));
		}};

		when(repository.findAll()).thenReturn(Flux.fromIterable(mockInvoices));

		webClient.get()
				.uri("/invoices")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Invoice.class)
				.hasSize(4)
				.value(invoices -> assertInvoice(invoices, 0, 1, "2021-02-01", 1000D))
				.value(invoices -> assertInvoice(invoices, 1, 2, "2021-02-02", 2000D))
				.value(invoices -> assertInvoice(invoices, 2, 3, "2021-02-03", 3000D))
				.value(invoices -> assertInvoice(invoices, 3, 4, "2021-02-04", 4000D));
	}

	@Test
	void findByIdNotFound() {
		when(repository.findById(6L)).thenReturn(Mono.error(new NotFoundException("Entity 6 not found")));
		webClient.get().uri("/invoices/{id}", 6)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void findByIdInvalid() {
		webClient.get().uri("/invoices/{id}", 0)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(ValidationErrorResponse.class)
				.value(p -> assertEquals(1, p.getViolations().size()))
				.value(p -> assertViolation(p.getViolations().get(0)));
	}

	@Test
	void create() {
		Invoice payload = createInvoice(5L, "2021-02-05", 5000);
		when(repository.save(payload)).thenReturn(createInvoiceMono(5L, "2021-02-05", 5000));
		webClient.post()
				.uri("/invoices")
				.contentType(APPLICATION_JSON)
				.body(Mono.just(payload), Invoice.class)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Invoice.class)
				.value(invoice -> assertInvoice(invoice, 5, "2021-02-05", 5000D));
	}

	@Test
	void createConflict() {
		Invoice payload = createInvoice(4L, "2021-02-04", 4000);
		when(repository.save(payload)).thenReturn(Mono.error(new DataIntegrityViolationException("Entity 4 already exists")));
		webClient.post()
				.uri("/invoices")
				.contentType(APPLICATION_JSON)
				.body(Mono.just(payload), Invoice.class)
				.exchange()
				.expectStatus().is4xxClientError();
	}

	@Test
	void upsert() {
		Invoice payload = createInvoice(5L, "2021-02-05", 5000);
		when(repository.save(payload)).thenReturn(createInvoiceMono(5L, "2021-02-05", 5000));
		webClient.put()
				.uri("/invoices")
				.contentType(APPLICATION_JSON)
				.body(Mono.just(payload), Invoice.class)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Invoice.class)
				.value(invoice -> assertInvoice(invoice, 5, "2021-02-05", 5000D));
	}

	@Test
	void update() {
		Invoice payload = createInvoice(1L, "2021-02-02", 2000);
		when(repository.findById(1L)).thenReturn(createInvoiceMono(1L, "2021-02-02", 2000));
		when(repository.save(payload)).thenReturn(createInvoiceMono(1L, "2021-02-02", 2000));

		webClient.put()
				.uri("/invoices/{id}", 1)
				.contentType(APPLICATION_JSON)
				.body(Mono.just(payload), Invoice.class)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Invoice.class)
				.value(invoice -> assertInvoice(invoice, 1, "2021-02-02", 2000D));
	}

	@Test
	void updateNotFound() {
		Mono<Invoice> payload = createInvoiceMono(6L, "2021-02-06", 6000);
		when(repository.findById(6L)).thenReturn(Mono.error(new NotFoundException("Entity 6 not found")));

		webClient.put()
				.uri("/invoices/{id}", 6)
				.contentType(APPLICATION_JSON)
				.body(payload, Invoice.class)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void remove() {
		when(repository.existsById(1L)).thenReturn(Mono.just(true));
		when(repository.deleteById(1L)).thenReturn(Mono.empty());

		webClient.delete()
				.uri("/invoices/{id}", 1)
				.exchange()
				.expectStatus().isNoContent();
	}

	@Test
	void removeNotFound() {
		when(repository.existsById(7L)).thenReturn(Mono.just(false));
		webClient.delete()
				.uri("/invoices/{id}", 7)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void createInvalidTotal() {
		Invoice payload = createInvoice(1L, "2021-02-01", 0);
		when(repository.save(payload)).thenReturn(Mono.error(new ConstraintViolationException(null)));

		webClient.post()
				.uri("/invoices")
				.contentType(APPLICATION_JSON)
				.body(Mono.just(payload), Invoice.class)
				.exchange()
				.expectStatus().is4xxClientError();
	}

}
