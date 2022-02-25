package com.schambeck.webflux.filter;

import com.schambeck.webflux.domain.Invoice;
import com.schambeck.webflux.repository.InvoiceRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Tag("integration")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("delay")
class DelayWebFluxFilterIT {

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

}
