package com.seucontrolefinanceiro.services;

import com.seucontrolefinanceiro.model.Bill;
import com.seucontrolefinanceiro.model.BillType;
import com.seucontrolefinanceiro.model.domain.Panel;
import com.seucontrolefinanceiro.model.domain.PanelHome;
import com.seucontrolefinanceiro.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PanelService {

    @Autowired private BillRepository billRepository;

    final Integer SUM_CURRENT_MONTH = 1;

    public PanelHome getPanelHome(String userId) {
        List<Bill> billsByUser = billRepository.findByUserId(userId);
        billsByUser = billsByUser
                .stream()
                .filter(bill -> !bill.isPaid())
                .collect(Collectors.toList());

        Set<LocalDate> dates = billsByUser
                .stream()
                .map(Bill::getPayDAy)
                .map(localDate -> localDate.with(TemporalAdjusters.firstDayOfMonth()))
                .sorted(LocalDate::compareTo)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Panel> panels = new ArrayList<>();

        for (LocalDate date : dates) {
            BigDecimal amount = getPanelAmount(billsByUser, date);
            String title = getPanelTitle(date);
            panels.add(
                Panel.builder()
                    .date(date)
                    .year(date.getYear())
                    .amount(amount)
                    .title(title)
                    .build()
            );
        }

        panels.sort(Comparator.comparing(Panel::getDate));
        Integer panelQuantity = getPanelQuantity(dates);

        return PanelHome.builder()
            .panels(panels)
            .panelQuantity(panelQuantity)
            .build();
    }

    private BigDecimal getPanelAmount(List<Bill> bills, LocalDate date) {
        return bills
            .stream()
            .filter(bill -> bill.getPayDAy()
                .with(TemporalAdjusters.firstDayOfMonth())
                    .equals(date) && bill.getBillType().equals(BillType.PAYMENT))
            .map(Bill::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String getPanelTitle(LocalDate date) {
        return date.getMonth().name();
    }

    private Integer getPanelQuantity(Set<LocalDate> dates) {
        LocalDate initialDate = dates.stream().findFirst().get().with(TemporalAdjusters.firstDayOfMonth());

        dates = dates
            .stream()
            .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        LocalDate lastDate = dates.stream().findFirst().get().with(TemporalAdjusters.firstDayOfMonth());
        Period diff = Period.between(initialDate, lastDate);
        return diff.getMonths() + SUM_CURRENT_MONTH;
    }
}
