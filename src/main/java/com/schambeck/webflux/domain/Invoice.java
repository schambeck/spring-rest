package com.schambeck.webflux.domain;

import com.schambeck.webflux.base.AbstractPersistable;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder(toBuilder = true)
public class Invoice extends AbstractPersistable<Invoice, Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @org.springframework.data.annotation.Id
    private Long id;

    @PastOrPresent(message = "Issued must be in the past")
    @NotNull(message = "Issued is mandatory")
    private LocalDate issued;

    @Positive(message = "Total must be positive")
    @NotNull(message = "Total is mandatory")
    private BigDecimal total;

}
