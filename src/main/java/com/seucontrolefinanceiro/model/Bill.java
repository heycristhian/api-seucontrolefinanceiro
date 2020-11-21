package com.seucontrolefinanceiro.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@Document
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"billDescription"})
public class Bill {
    @Id
    private String id;
    private String billDescription;
    private BigDecimal amount;
    private boolean everyMonth;
    private LocalDate payDAy;
    private BillType billType;
    private PaymentCategory paymentCategory;
    private boolean paid;
    private String parent;
    private String userId;
    private Integer portion;
    private LocalDate paidIn;
}
