package com.seucontrolefinanceiro.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Panel {
    private LocalDate date;
    private Integer year;
    private BigDecimal amount;
    private String title;
}
