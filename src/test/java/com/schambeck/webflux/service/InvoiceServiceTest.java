package com.schambeck.webflux.service;

import com.schambeck.webflux.base.exception.ConflictException;
import com.schambeck.webflux.base.exception.NotFoundException;
import com.schambeck.webflux.domain.Invoice;
import com.schambeck.webflux.repository.InvoiceRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("unit")
@SpringBootTest(classes = {InvoiceRepository.class, InvoiceServiceImpl.class})
class InvoiceServiceTest {

	@Autowired
	private InvoiceService service;

	@MockBean
	private InvoiceRepository repository;

	private Invoice createInvoice(String issued, double total) {
		return createInvoice(null, issued, total);
	}

	private static Invoice createInvoice(Long id, String issued, double total) {
		return new Invoice(id, LocalDate.parse(issued), BigDecimal.valueOf(total));
	}

	private static Mono<Invoice> createInvoiceMono(Long id, String issued, double total) {
		return Mono.just(createInvoice(id, issued, total));
	}

	private boolean assertInvoice(Invoice invoice, int id, String issued, double total) {
		return Long.valueOf(id).equals(invoice.getId())
				&& invoice.getIssued().equals(LocalDate.parse(issued))
				&& invoice.getTotal().equals(BigDecimal.valueOf(total));
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

		Flux<Invoice> invoices = service.findAll();
        StepVerifier.create(invoices)
                .expectNextMatches(invoice -> assertInvoice(invoice, 1, "2021-02-01", 1000))
                .expectNextMatches(invoice -> assertInvoice(invoice, 2, "2021-02-02", 2000))
                .expectNextMatches(invoice -> assertInvoice(invoice, 3, "2021-02-03", 3000))
                .expectNextMatches(invoice -> assertInvoice(invoice, 4, "2021-02-04", 4000))
                .verifyComplete();
	}

	@Test
	void findById() {
		when(repository.findById(1L)).thenReturn(Mono.just(createInvoice(1L, "2021-02-01", 1000)));
		Mono<Invoice> found = service.findById(1L);
		StepVerifier.create(found)
				.expectNextMatches(invoice -> assertInvoice(invoice, 1, "2021-02-01", 1000))
				.verifyComplete();
	}

	@Test
	void findByIdNotFound() {
		doThrow(new NotFoundException("Entity 6 not found")).when(repository).findById(6L);
		NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findById(6L));
		String expected = "Entity 6 not found";
		String actual = exception.getMessage();
		assertEquals(actual, expected);
	}

	@Test
	void create() {
		Invoice mockInvoice = createInvoice("2021-02-05", 5000);
		when(repository.save(mockInvoice)).thenReturn(createInvoiceMono(5L, "2021-02-05", 5000));
		Mono<Invoice> created = service.create(Mono.just(mockInvoice));
		StepVerifier.create(created)
				.expectNextMatches(invoice -> assertInvoice(invoice, 5, "2021-02-05", 5000))
				.verifyComplete();
	}

	@Test
	void createConflict() {
		Invoice invoice = createInvoice(4L, "2021-02-04", 4000);
		when(repository.save(invoice)).thenReturn(Mono.error(new ConflictException("Entity 4 already exists")));
		StepVerifier.create(service.create(Mono.just(invoice)))
				.expectErrorMatches(throwable -> throwable instanceof ConflictException && throwable.getMessage().equals("Entity 4 already exists"))
				.verify();
	}

	@Test
	void update() {
		Invoice invoiceToUpdate = createInvoice( "2021-02-05", 5000);
		Invoice mockInvoice = createInvoice(1L, "2021-02-05", 5000);
		when(repository.findById(1L)).thenReturn(Mono.just(mockInvoice));
		when(repository.save(mockInvoice)).thenReturn(Mono.just(mockInvoice));
		Mono<Invoice> updated = service.update(1L, Mono.just(invoiceToUpdate));
		StepVerifier.create(updated)
				.expectNextMatches(invoice -> assertInvoice(invoice, 1, "2021-02-05", 5000))
				.verifyComplete();
	}

	@Test
	void updateNotFound() {
		when(repository.findById(6L)).thenReturn(Mono.error(new NotFoundException("Entity 6 not found")));
		Invoice invoice = createInvoice("2021-02-06", 6000);
		StepVerifier.create(service.update(6L, Mono.just(invoice)))
				.expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().equals("Entity 6 not found"))
				.verify();
	}

	@Test
	void delete() {
		when(repository.existsById(1L)).thenReturn(Mono.just(true));
		when(repository.deleteById(1L)).thenReturn(Mono.empty());
		service.delete(1L);
	}

	@Test
	void deleteNotFound() {
		when(repository.existsById(6L)).thenReturn(Mono.just(false));
		when(repository.deleteById(6L)).thenReturn(Mono.error(new NotFoundException("Entity 6 not found")));
		StepVerifier.create(service.delete(6L))
				.expectErrorMatches(throwable -> throwable instanceof NotFoundException && throwable.getMessage().equals("Entity 6 not found"))
				.verify();
	}

}
