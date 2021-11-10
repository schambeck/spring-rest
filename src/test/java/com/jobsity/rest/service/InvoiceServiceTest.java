package com.jobsity.rest.service;

import com.jobsity.rest.base.ConflictException;
import com.jobsity.rest.base.NotFoundException;
import com.jobsity.rest.domain.Invoice;
import com.jobsity.rest.repository.InvoiceRepository;
import com.jobsity.rest.repository.InvoiceRepositoryImpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("unit")
@SpringBootTest(classes = {InvoiceRepositoryImpl.class, InvoiceServiceImpl.class})
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

	private void assertInvoice(List<Invoice> invoices, int index, int id, String issued, double total) {
		Invoice invoice = invoices.get(index);
		assertInvoice(invoice, id, issued, total);
	}

	private void assertInvoice(Invoice invoice, int id, String issued, double total) {
		assertEquals(id, invoice.getId());
		assertEquals(LocalDate.parse(issued), invoice.getIssued());
		assertEquals(total, invoice.getTotal().doubleValue());
	}

	@Test
	void findAll() {
		when(repository.findAll()).thenReturn(invoices);
		List<Invoice> invoices = service.findAll();
		assertEquals(4, invoices.size());
		assertInvoice(invoices, 0, 1, "2021-02-01", 1000D);
		assertInvoice(invoices, 1, 2, "2021-02-02", 2000D);
		assertInvoice(invoices, 2, 3, "2021-02-03", 3000D);
		assertInvoice(invoices, 3, 4, "2021-02-04", 4000D);
	}

	@Test
	void findById() {
		when(repository.findById(1L)).thenReturn(createInvoice(1L, "2021-02-01", 1000));
		Invoice invoice = service.findById(1L);
		assertInvoice(invoice, 1, "2021-02-01", 1000D);
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
		Invoice invoice = createInvoice(5L, "2021-02-05", 5000);
		when(repository.create(invoice)).thenReturn(createInvoice(5L, "2021-02-05", 5000));
		Invoice created = service.create(invoice);
		assertInvoice(created, 5, "2021-02-05", 5000D);
	}

	@Test
	void createConflict() {
		Invoice invoice = createInvoice(4L, "2021-02-04", 4000);
		doThrow(new ConflictException("Entity 4 already exists")).when(repository).create(invoice);
		ConflictException exception = assertThrows(ConflictException.class, () -> service.create(invoice));
		String expected = "Entity 4 already exists";
		String actual = exception.getMessage();
		assertEquals(actual, expected);
	}

	@Test
	void update() {
		Invoice invoice = createInvoice( "2021-02-05", 5000);
		when(repository.update(1L, invoice)).thenReturn(createInvoice(1L, "2021-02-05", 5000));
		Invoice updated = service.update(1L, invoice);
		assertInvoice(updated, 1, "2021-02-05", 5000D);
	}

	@Test
	void updateNotFound() {
		Invoice invoice = createInvoice("2021-02-06", 6000);
		doThrow(new NotFoundException("Entity 6 not found")).when(repository).update(6L, invoice);
		NotFoundException exception = assertThrows(NotFoundException.class, () -> service.update(6L, invoice));
		String expected = "Entity 6 not found";
		String actual = exception.getMessage();
		assertEquals(actual, expected);
	}

	@Test
	void delete() {
		doNothing().when(repository).delete(1L);
		service.delete(1L);
	}

	@Test
	void deleteNotFound() {
		doThrow(new NotFoundException("Entity 6 not found")).when(repository).delete(6L);
		NotFoundException exception = assertThrows(NotFoundException.class, () -> service.delete(6L));
		String expected = "Entity 6 not found";
		String actual = exception.getMessage();
		assertEquals(actual, expected);
	}

	private static final List<Invoice> invoices = new ArrayList<Invoice>() {{
		add(createInvoice(1L, "2021-02-01", 1000));
		add(createInvoice(2L, "2021-02-02", 2000));
		add(createInvoice(3L, "2021-02-03", 3000));
		add(createInvoice(4L, "2021-02-04", 4000));
	}};

}
