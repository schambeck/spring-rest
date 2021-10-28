package com.jobsity.rest.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice implements Serializable {

    private Long id;

    private LocalDate issued;

    private BigDecimal total;

    public Invoice(Long id, LocalDate issued, BigDecimal total) {
        this.id = id;
        this.issued = issued;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getIssued() {
        return issued;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setIssued(LocalDate issued) {
        this.issued = issued;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
