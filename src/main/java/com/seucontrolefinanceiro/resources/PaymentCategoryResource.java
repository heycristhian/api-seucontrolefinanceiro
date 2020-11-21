package com.seucontrolefinanceiro.resources;

import com.seucontrolefinanceiro.model.PaymentCategory;
import com.seucontrolefinanceiro.dto.PaymentCategoryDTO;
import com.seucontrolefinanceiro.dto.UserDTO;
import com.seucontrolefinanceiro.form.PaymentCategoryForm;
import com.seucontrolefinanceiro.services.PaymentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("scf-service/payment-categories")
public class PaymentCategoryResource implements Resource<PaymentCategoryDTO, PaymentCategoryForm> {

    @Autowired
    private PaymentCategoryService service;

    @Override
    @GetMapping
    public ResponseEntity<List<PaymentCategoryDTO>> find(String query) {
        List<PaymentCategory> paymentCategories = service.findAll();
        return ResponseEntity.ok().body(PaymentCategoryDTO.converter((paymentCategories)));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PaymentCategoryDTO> findById(@PathVariable String id) {
        PaymentCategory paymentCategory = service.findById(id);
        return ResponseEntity.ok().body(new PaymentCategoryDTO(paymentCategory));
    }

    @Override
    @PostMapping
    public ResponseEntity<PaymentCategoryDTO> insert(@RequestBody @Validated PaymentCategoryForm form, UriComponentsBuilder uriBuilder) {
        PaymentCategory paymentCategory = form.converter();
        paymentCategory = service.save(paymentCategory);
        URI uri = uriBuilder.path("scf-service/payment-categories/{id}").buildAndExpand(paymentCategory.getId()).toUri();
        return ResponseEntity.created(uri).body(new PaymentCategoryDTO(paymentCategory));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody @Validated PaymentCategoryForm form, UriComponentsBuilder uriBuilder) {
        PaymentCategory paymentCategory = form.converter();
        paymentCategory = service.update(paymentCategory);
        URI uri = uriBuilder.path("scf-service/payment-categories/{id}").buildAndExpand(paymentCategory.getId()).toUri();
        return ResponseEntity.noContent().build();
    }
}
