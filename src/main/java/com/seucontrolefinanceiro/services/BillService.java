package com.seucontrolefinanceiro.services;

import com.seucontrolefinanceiro.model.Bill;
import com.seucontrolefinanceiro.model.User;
import com.seucontrolefinanceiro.model.domain.Panel;
import com.seucontrolefinanceiro.model.domain.PanelHome;
import com.seucontrolefinanceiro.repository.BillRepository;
import com.seucontrolefinanceiro.repository.UserRepository;
import com.seucontrolefinanceiro.services.exception.ObjectNotFoundException;
import com.seucontrolefinanceiro.services.util.GenerateObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class BillService implements Service<Bill> {

    @Autowired private BillRepository repository;

    @Autowired private UserService userService;

    @Autowired private UserRepository userRepository;

    private final int PORTION_DEFAULT = 11;

    @Override
    public List<Bill> findAll() {
        return repository.findAll();
    }

    @Override
    public Bill findById(String id) {
        Optional<Bill> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Object not found!"));
    }

    @Override
    public Bill save(Bill bill) {
        User user = userService.findById(bill.getUserId());
        List<Bill> allBillsByUserId = repository.findByUserId(bill.getUserId());
        user.setBills(allBillsByUserId);
        analyzeMonthlyBill(bill, user);
        String parentId = bill.getParent() == null ? bill.getId() : bill.getParent();
        bill.setParent(parentId);
        repository.save(bill);
        userRepository.save(user);
        return bill;
    }

    @Override
    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public Bill update(Bill newBill) {
        if (!newBill.isPaid()) {
            Bill oldBill = findById(newBill.getId());
            boolean oldBillIsEveryMonth = oldBill.isEveryMonth();
            boolean newBillIsEveryMonth = newBill.isEveryMonth();
            newBill.setParent((newBill.getParent() == null) ? newBill.getId() : newBill.getParent());

            if (newBillIsEveryMonth && !oldBillIsEveryMonth) {
               createChildrenBill(newBill);
            } else if (oldBillIsEveryMonth && !newBillIsEveryMonth) {
                removeChildrenBill(newBill);
            } else if(newBill.isEveryMonth()){
                onlyUpdateChildrenBill(newBill);
            }
            repository.save(newBill);
            return newBill;
        } else {
            return pay(newBill);
        }
    }

    @Override
    public Bill updateData(Bill newObj, String id) {
        return Bill.builder()
                .id(id)
                .billDescription(newObj.getBillDescription())
                .amount(newObj.getAmount())
                .everyMonth(newObj.isEveryMonth())
                .payDAy(newObj.getPayDAy())
                .billType(newObj.getBillType())
                .paymentCategory(newObj.getPaymentCategory())
                .paid(newObj.isPaid())
                .parent(newObj.getParent())
                .userId(newObj.getUserId())
                .portion(newObj.getPortion())
                .paidIn(newObj.getPaidIn())
                .build();
    }

    public List<Bill> returnBillsChild(Bill bill) {
        return repository.findAll().stream()
                .filter(x -> x.getParent().equals(bill.getParent())
                        && !x.getId().equals(bill.getId())
                        && x.isPaid() == false)
                .collect(Collectors.toList());
    }

    public Bill pay(Bill bill) {
        bill.setPaid(true);
        repository.save(bill);
        return bill;
    }

    public void deleteAll(List<Bill> bills) {
        repository.deleteAll(bills);
    }

    private void analyzeMonthlyBill(Bill bill, User user) {
        if (bill.isEveryMonth()) {
            Integer index = bill.getPortion() == null ? PORTION_DEFAULT : bill.getPortion();
            List<Bill> bills = GenerateObject.generateBills(bill, index);
            repository.insert(bill);
            user.addToListBill(bill);

            for (Bill b : bills) {
                b.setParent(bill.getId());
                repository.insert(b);
                user.addToListBill(b);
            }
        } else {
            repository.insert(bill);
            user.addToListBill(bill);
        }
    }

    private void createChildrenBill(Bill newBill) {
        List<Bill> bills = new ArrayList<>();
        User user = userService.findById(newBill.getUserId());
        user.setBills(repository.findByUserId(newBill.getUserId()));

        Integer index = newBill.getPortion() == null ? PORTION_DEFAULT : newBill.getPortion();
        bills = GenerateObject.generateBills(newBill, index);

        for (Bill b : bills) {
            b.setParent(newBill.getId());
            repository.insert(b);
            user.addToListBill(b);
        }
        userRepository.save(user);
    }

    private void removeChildrenBill(Bill newBill) {
        String parentId = newBill.getParent() == null ? newBill.getId() : newBill.getParent();

        repository.findByUserId(newBill.getUserId())
            .stream().filter(x ->
            x.isPaid() == false
                    && x.getParent().compareTo(parentId) == 0)
            .collect(Collectors.toList()).forEach(x -> repository.delete(x));

        newBill.setPortion(null);
    }

    private void onlyUpdateChildrenBill(Bill newBill) {
        List<Bill> bills = new ArrayList<>();
        String parentId = newBill.getParent() == null ? newBill.getId() : newBill.getParent();
        try {
            bills = repository.findByUserId(newBill.getUserId())
                    .stream()
                    .filter(x -> x.getParent().equals(parentId)
                            && x.isPaid() == false
                            && !x.getId().equals(newBill.getId()))
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        for (Bill b : bills) {
            b = GenerateObject.cloneBill(b, newBill);
            repository.save(b);
        }
    }

    public PanelHome getPanelHome(String userId) {
        List<Bill> billsByUser = repository.findByUserId(userId);
        Set<LocalDate> dates = billsByUser
                .stream()
                .map(Bill::getPayDAy)
                .collect(Collectors.toSet());

        List<Panel> panels = new ArrayList<>();

        for (LocalDate date : dates) {
            BigDecimal amount = getPanelAmount(billsByUser);
            String title = getPanelTitle(date);
            panels.add(
                Panel.builder()
                    .date(date)
                    .amount(amount)
                    .title(title)
                    .build()
            );
        }

        return PanelHome.builder()
                .panels(panels)
                .panelQuantity(dates.size())
                .build();
    }

    private BigDecimal getPanelAmount(List<Bill> bills) {
        return bills
            .stream()
            .map(Bill::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String getPanelTitle(LocalDate date) {
        return "Temporario";
    }
}
