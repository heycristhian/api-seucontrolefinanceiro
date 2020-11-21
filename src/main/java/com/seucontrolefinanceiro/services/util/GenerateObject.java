package com.seucontrolefinanceiro.services.util;

import com.seucontrolefinanceiro.model.Bill;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GenerateObject {

    public static List<Bill> generateBills(Bill bill, Integer index) {
        List<Bill> bills = new ArrayList<>();
        for(int i = 1; i < index; i++) {
            bills.add(GenerateObject.insertData(bill, i));
        }
        return bills;
    }

    private static Bill insertData(Bill bill, Integer index) {
        return Bill.builder()
                .billDescription(bill.getBillDescription())
                .amount(bill.getAmount())
                .everyMonth(bill.isEveryMonth())
                .payDAy(bill.getPayDAy().plusMonths(index))
                .billType(bill.getBillType())
                .paymentCategory(bill.getPaymentCategory())
                .paid(bill.isPaid())
                .userId(bill.getUserId())
                .portion(bill.getPortion())
                .paidIn(bill.getPaidIn())
                .build();
    }

    public static Bill cloneBill(Bill oldObj, Bill newObj) {
        oldObj.setBillDescription(newObj.getBillDescription());
        oldObj.setAmount(newObj.getAmount());
        oldObj.setEveryMonth(newObj.isEveryMonth());
        oldObj.setPayDAy(LocalDate.of(oldObj.getPayDAy().getYear(), oldObj.getPayDAy().getMonth(), newObj.getPayDAy().getDayOfMonth()));
        oldObj.setBillType(newObj.getBillType());
        oldObj.setPaymentCategory(newObj.getPaymentCategory());
        oldObj.setPaid(newObj.isPaid());
        oldObj.setParent(newObj.getParent());
        oldObj.setUserId(newObj.getUserId());
        oldObj.setPortion((newObj.getPortion()));
        oldObj.setPaidIn(newObj.getPaidIn());
        return oldObj;
    }
}
