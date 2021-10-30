package com.jobsity.rest.controller;

import com.jobsity.rest.base.NotFoundException;
import com.jobsity.rest.base.ObjectMapperUtil;
import com.jobsity.rest.domain.Invoice;
import com.jobsity.rest.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.Month.FEBRUARY;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
@Import(ObjectMapperUtil.class)
class InvoiceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapperUtil mapperUtil;

	@MockBean
	private InvoiceService service;

	private List<Invoice> invoices;

	@BeforeEach
	void setUp() {
		invoices = new ArrayList<Invoice>() {{
			add(new Invoice(1L, LocalDate.of(2021, FEBRUARY, 1), BigDecimal.valueOf(1000)));
			add(new Invoice(2L, LocalDate.of(2021, FEBRUARY, 2), BigDecimal.valueOf(2000)));
			add(new Invoice(3L, LocalDate.of(2021, FEBRUARY, 3), BigDecimal.valueOf(3000)));
			add(new Invoice(4L, LocalDate.of(2021, FEBRUARY, 4), BigDecimal.valueOf(4000)));
		}};
	}

	@Test
	void testFindAll() throws Exception {
		when(service.findAll()).thenReturn(invoices);
		ResultActions actions = mockMvc.perform(get("/invoices"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(invoices.size())));

		for (int i = 0; i < invoices.size(); i++) {
			Invoice invoice = invoices.get(i);
			String index = "$[" + i + "]";
			actions.andExpect(jsonPath(index + ".id", is(invoice.getId().intValue())))
				.andExpect(jsonPath(index + ".issued", is(invoice.getIssued().toString())))
				.andExpect(jsonPath(index + ".total", is(invoice.getTotal().intValue())));
		}

		verify(service).findAll();
	}

	@Test
	void testFindById() throws Exception {
		Invoice invoice = invoices.get(0);
		when(service.findById(1L)).thenReturn(invoice);
		mockMvc.perform(get("/invoices/{id}", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(invoice.getId().intValue())))
				.andExpect(jsonPath("$.issued", is(invoice.getIssued().toString())))
				.andExpect(jsonPath("$.total", is(invoice.getTotal().intValue())));
		verify(service).findById(1L);
	}

	@Test
	void testFindByIdNotFound() throws Exception {
		when(service.findById(6L)).thenThrow(NotFoundException.class);
		mockMvc.perform(get("/invoices/{id}", 6))
				.andExpect(status().isNotFound());
		verify(service).findById(6L);
	}

	@Test
	void testCreate() throws Exception {
		Invoice invoice = new Invoice(5L, LocalDate.of(2021, FEBRUARY, 5), BigDecimal.valueOf(5000));
		when(service.create(invoice)).thenReturn(invoice);
		mockMvc.perform(post("/invoices")
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(invoice.getId().intValue())))
				.andExpect(jsonPath("$.issued", is(invoice.getIssued().toString())))
				.andExpect(jsonPath("$.total", is(invoice.getTotal().intValue())));
		verify(service).create(invoice);
	}

	@Test
	void testUpdate() throws Exception {
		Invoice invoice = invoices.get(0);
		when(service.update(1L, invoice)).thenReturn(invoice);
		mockMvc.perform(put("/invoices/{id}", 1)
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(invoice.getId().intValue())))
				.andExpect(jsonPath("$.issued", is(invoice.getIssued().toString())))
				.andExpect(jsonPath("$.total", is(invoice.getTotal().intValue())));
		verify(service).update(1L, invoice);
	}

	@Test
	void testUpdateNotFound() throws Exception {
		Invoice invoice = new Invoice(LocalDate.of(2021, FEBRUARY, 5), BigDecimal.valueOf(5000));
		when(service.update(6L, invoice)).thenThrow(NotFoundException.class);
		mockMvc.perform(put("/invoices/{id}", 6)
						.content(mapperUtil.asJsonString(invoice))
						.contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound());
		verify(service).update(6L, invoice);
	}

	@Test
	void testDelete() throws Exception {
		doNothing().when(service).delete(1L);
		mockMvc.perform(delete("/invoices/{id}", 1))
				.andExpect(status().isNoContent());
		verify(service).delete(1L);
	}

	@Test
	void testDeleteNotFound() throws Exception {
		doThrow(NotFoundException.class).when(service).delete(6L);
		mockMvc.perform(delete("/invoices/{id}", 6))
				.andExpect(status().isNotFound());
		verify(service).delete(6L);
	}

}
