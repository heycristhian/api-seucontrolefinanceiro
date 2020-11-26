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

        if (billsByUser.size() == 0) {
            return returnObjectEmpty();
        }

        billsByUser = billsByUser
                .stream()
                .filter(bill -> !bill.isPaid())
                .collect(Collectors.toList());

        billsByUser.stream()
                .sorted(Comparator.comparing(Bill::getPayDAy))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Set<LocalDate> dates = billsByUser
                .stream()
                .map(Bill::getPayDAy)
                .map(localDate -> localDate.with(TemporalAdjusters.firstDayOfMonth()))
                .sorted(LocalDate::compareTo)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        List<Panel> panels = handlePanels(dates, billsByUser);

        panels.sort(Comparator.comparing(Panel::getDate));

        return PanelHome.builder()
            .panels(panels)
            .panelQuantity(panels.size())
            .build();
    }

    private List<Panel> handlePanels(Set<LocalDate> dates, List<Bill> billsByUser) {
        List<Panel> panels = new ArrayList<>();
        for (LocalDate date : dates) {
            Set<Bill> billsByDate = billsByUser
                .stream()
                .filter(bill -> bill.getPayDAy().getYear() == date.getYear()
                        && bill.getPayDAy().getMonth() == date.getMonth())
                .collect(Collectors.toSet());

            BigDecimal amount = getPanelAmount(billsByUser, date);
            String title = getPanelTitle(date);
            panels.add(
                Panel.builder()
                        .date(date)
                        .year(date.getYear())
                        .amount(amount)
                        .title(title)
                        .bills(billsByDate)
                        .build()
            );
        }
        return panels;
    }

    private PanelHome returnObjectEmpty() {
        LocalDate currentDate = LocalDate.now();
        Panel panel =  Panel.builder()
                .amount(BigDecimal.ZERO)
                .year(currentDate.getYear())
                .date(currentDate)
                .title(getPanelTitle(currentDate))
                .build();

        return PanelHome.builder()
                .panels(Arrays.asList(panel))
                .panelQuantity(1)
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
