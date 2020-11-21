package com.seucontrolefinanceiro.dto;

import com.seucontrolefinanceiro.model.Bill;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BillDTO {
    private final String id;
    private final String billDescription;
    private final BigDecimal amount;
    private final LocalDate payDAy;
    private final String billType;
    private final String paymentCategory;
    private final boolean paid;
    private final String userId;
    private final Integer portion;
    private final LocalDate paidIn;

    public BillDTO(Bill bill) {
        this.id = bill.getId();
        this.billDescription = bill.getBillDescription();
        this.amount = bill.getAmount();
        this.payDAy = bill.getPayDAy();
        this.billType = bill.getBillType().getDescription();
        this.paymentCategory = bill.getPaymentCategory().getDescription();
        this.paid = bill.isPaid();
        this.userId = bill.getUserId();
        this.portion = bill.getPortion();
        this.paidIn = bill.getPaidIn();
    }

    public static List<BillDTO> converter(List<Bill> bills) {
        return bills.stream().map(BillDTO::new).collect(Collectors.toList());
    }

}
