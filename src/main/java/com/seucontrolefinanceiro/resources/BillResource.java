package com.seucontrolefinanceiro.resources;

import com.seucontrolefinanceiro.model.Bill;
import com.seucontrolefinanceiro.dto.BillDTO;
import com.seucontrolefinanceiro.dto.UserDTO;
import com.seucontrolefinanceiro.form.BillForm;
import com.seucontrolefinanceiro.services.BillService;
import com.seucontrolefinanceiro.services.PaymentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("scf-service/bills")
public class BillResource implements Resource<BillDTO, BillForm> {

    @Autowired
    private BillService service;

    @Autowired
    private PaymentCategoryService paymentCategoryService;

    @Override
    @GetMapping
    public ResponseEntity<List<BillDTO>> find(String query) {
        List<Bill> bills = service.findAll();
        return ResponseEntity.ok().body(BillDTO.converter(bills));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> findById(@PathVariable String id) {
        Bill bill = service.findById(id);
        return ResponseEntity.ok().body(new BillDTO(bill));
    }

    @Override
    @PostMapping
    public ResponseEntity<BillDTO> insert(@RequestBody @Validated BillForm form, UriComponentsBuilder uriBuilder) {
        Bill bill = form.converter();
        service.save(bill);
        URI uri = uriBuilder.path("scf-service/bills/{id}").buildAndExpand(bill.getId()).toUri();
        return ResponseEntity.created(uri).body(new BillDTO(bill));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(String id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping()
    public ResponseEntity<UserDTO> update(@RequestBody @Validated BillForm form, UriComponentsBuilder uriBuilder) {
        Bill bill = form.converter();
        bill.setPaymentCategory(paymentCategoryService.findByDescriptionContainingIgnoreCase(bill.getPaymentCategory().getDescription()).get(0));
        service.update(bill);
        URI uri = uriBuilder.path("scf-service/bills/{id}").buildAndExpand(bill.getId()).toUri();
        return ResponseEntity.noContent().build();
    }
}
