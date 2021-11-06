package com.jobsity.rest.controller;

import com.jobsity.rest.base.ObjectMapperUtil;
import com.jobsity.rest.domain.Invoice;
import com.jobsity.rest.service.InvoiceService;
import com.jobsity.rest.service.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@Import({ObjectMapperUtil.class, InvoiceServiceImpl.class})
@WebMvcTest(InvoiceController.class)
class InvoiceControllerITTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapperUtil mapperUtil;

	@Autowired
	private InvoiceService service;

	@BeforeEach
	void setUp() {
		resetInvoices();
	}

	private void resetInvoices() {
		new ArrayList<>(service.findAll()).forEach(invoice -> service.delete(invoice.getId()));
		InvoiceControllerITTest.invoices.forEach(invoice -> service.create(invoice));
	}

	private Invoice createInvoice(String issued, double total) {
		return createInvoice(null, issued, total);
	}

	private static Invoice createInvoice(Long id, String issued, double total) {
		return new Invoice(id, LocalDate.parse(issued), BigDecimal.valueOf(total));
	}

	private ResultMatcher[] invoiceMatchers(int index, int id, String issued, double total) {
		String indexStr = String.format("$[%d]", index);
		return new ResultMatcher[] {
				jsonPath(indexStr + ".id", is(id)),
				jsonPath(indexStr + ".issued", is(issued)),
				jsonPath(indexStr + ".total", is(total))};
	}

	private ResultMatcher[] invoiceMatchers(int id, String issued, double total) {
		return new ResultMatcher[] {
				jsonPath("$.id", is(id)),
				jsonPath("$.issued", is(issued)),
				jsonPath("$.total", is(total))};
	}

	@Test
	void findAll() throws Exception {
		mockMvc.perform(get("/invoices"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpectAll(invoiceMatchers(0, 1, "2021-02-01", 1000D))
				.andExpectAll(invoiceMatchers(1, 2, "2021-02-02", 2000D))
				.andExpectAll(invoiceMatchers(2, 3, "2021-02-03", 3000D))
				.andExpectAll(invoiceMatchers(3, 4, "2021-02-04", 4000D));
	}

	@Test
	void findById() throws Exception {
		mockMvc.perform(get("/invoices/{id}", 1))
				.andExpect(status().isOk())
				.andExpectAll(invoiceMatchers(1, "2021-02-01", 1000D));
	}

	@Test
	void findByIdNotFound() throws Exception {
		mockMvc.perform(get("/invoices/{id}", 6))
				.andExpect(status().isNotFound());
	}

	@Test
	void create() throws Exception {
		Invoice invoice = createInvoice(5L, "2021-02-05", 5000);
		mockMvc.perform(post("/invoices")
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpectAll(invoiceMatchers(5, "2021-02-05", 5000D));
	}

	@Test
	void createConflict() throws Exception {
		Invoice invoice = createInvoice(4L, "2021-02-04", 4000);
		mockMvc.perform(post("/invoices")
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isConflict());
	}

	@Test
	void update() throws Exception {
		Invoice invoice = createInvoice("2021-02-02", 2000);
		mockMvc.perform(put("/invoices/{id}", 1)
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpectAll(invoiceMatchers(1, "2021-02-02", 2000D));
	}

	@Test
	void updateNotFound() throws Exception {
		Invoice invoice = createInvoice("2021-02-06", 6000);
		mockMvc.perform(put("/invoices/{id}", 6)
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteTest() throws Exception {
		mockMvc.perform(delete("/invoices/{id}", 1))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteTestNotFound() throws Exception {
		mockMvc.perform(delete("/invoices/{id}", 6))
				.andExpect(status().isNotFound());
	}

	private static final List<Invoice> invoices = new ArrayList<Invoice>() {{
		add(createInvoice(1L, "2021-02-01", 1000));
		add(createInvoice(2L, "2021-02-02", 2000));
		add(createInvoice(3L, "2021-02-03", 3000));
		add(createInvoice(4L, "2021-02-04", 4000));
	}};

}
