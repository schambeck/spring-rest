package com.schambeck.rest.controller;

import com.schambeck.rest.base.ObjectMapperUtil;
import com.schambeck.rest.base.exception.ConflictException;
import com.schambeck.rest.base.exception.NotFoundException;
import com.schambeck.rest.domain.Invoice;
import com.schambeck.rest.service.InvoiceService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("unit")
@Import(ObjectMapperUtil.class)
@WebMvcTest(InvoiceController.class)
class InvoiceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapperUtil mapperUtil;

	@MockBean
	private InvoiceService service;

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
		List<Invoice> invoices = new ArrayList<Invoice>() {{
			add(createInvoice(1L, "2021-02-01", 1000));
			add(createInvoice(2L, "2021-02-02", 2000));
			add(createInvoice(3L, "2021-02-03", 3000));
			add(createInvoice(4L, "2021-02-04", 4000));
		}};

		when(service.findAll()).thenReturn(invoices);

		MvcResult result = mockMvc.perform(get("/invoices"))
				.andExpect(request().asyncStarted())
				.andReturn();

		mockMvc.perform(asyncDispatch(result))
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
		when(service.findById(1L)).thenReturn(createInvoice(1L, "2021-02-01", 1000));

		MvcResult result = mockMvc.perform(get("/invoices/{id}", 1))
				.andExpect(request().asyncStarted())
				.andReturn();

		mockMvc.perform(asyncDispatch(result))
				.andExpect(status().isOk())
				.andExpectAll(invoiceMatchers(1, "2021-02-01", 1000D));
	}

	@Test
	void findByIdNotFound() throws Exception {
		doThrow(new NotFoundException("Entity 6 not found")).when(service).findById(6L);
		mockMvc.perform(get("/invoices/{id}", 6))
				.andExpect(status().isNotFound());
	}

	@Test
	void create() throws Exception {
		Invoice invoice = createInvoice(5L, "2021-02-05", 5000);
		when(service.create(invoice)).thenReturn(createInvoice(5L, "2021-02-05", 5000));

		MvcResult result = mockMvc.perform(post("/invoices")
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(request().asyncStarted())
				.andReturn();

		mockMvc.perform(asyncDispatch(result))
				.andExpect(status().isCreated())
				.andExpectAll(invoiceMatchers(5, "2021-02-05", 5000D));
	}

	@Test
	void createConflict() throws Exception {
		Invoice invoice = createInvoice(4L, "2021-02-04", 4000);
		doThrow(new ConflictException("Entity 4 already exists")).when(service).create(invoice);
		mockMvc.perform(post("/invoices")
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isConflict());
	}

	@Test
	void update() throws Exception {
		Invoice invoice = createInvoice("2021-02-02", 2000);
		when(service.update(1L, invoice)).thenReturn(createInvoice(1L, "2021-02-02", 2000));

		MvcResult result = mockMvc.perform(put("/invoices/{id}", 1)
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(request().asyncStarted())
				.andReturn();

		mockMvc.perform(asyncDispatch(result))
				.andExpect(status().isOk())
				.andExpectAll(invoiceMatchers(1, "2021-02-02", 2000D));
	}

	@Test
	void updateNotFound() throws Exception {
		Invoice invoice = createInvoice("2021-02-06", 6000);
		doThrow(new NotFoundException("Entity 6 not found")).when(service).update(6L, invoice);
		mockMvc.perform(put("/invoices/{id}", 6)
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteTest() throws Exception {
		doNothing().when(service).delete(1L);

		MvcResult result = mockMvc.perform(delete("/invoices/{id}", 1))
				.andExpect(request().asyncStarted())
				.andReturn();

		mockMvc.perform(asyncDispatch(result))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteTestNotFound() throws Exception {
		doThrow(new NotFoundException("Entity 6 not found")).when(service).delete(6L);
		mockMvc.perform(delete("/invoices/{id}", 6))
				.andExpect(status().isNotFound());
	}

	@Test
	void createInvalidTotal() throws Exception {
		Invoice invoice = createInvoice(1L, "2021-02-01", 0);
		String body = mapperUtil.asJsonString(invoice);
		when(service.create(invoice)).thenReturn(invoice);

		mockMvc.perform(post("/invoices")
						.contentType("application/json")
						.content(body))
				.andExpect(status().isBadRequest());
	}

}
