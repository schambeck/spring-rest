package com.jobsity.rest.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Invoice implements Serializable {

    private final Long id;

    @PastOrPresent(message = "Issued must be in the past")
    @NotNull(message = "Issued is mandatory")
    private LocalDate issued;

    @Positive(message = "Total must be positive")
    @NotNull(message = "Total is mandatory")
    private BigDecimal total;

    public Invoice() {
        this(null, null);
    }

    public Invoice(LocalDate issued, BigDecimal total) {
        this(null, issued, total);
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(id, invoice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", issued=" + issued +
                ", total=" + total +
                '}';
    }

}
