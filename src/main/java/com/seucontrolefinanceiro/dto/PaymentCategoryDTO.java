package com.seucontrolefinanceiro.dto;

import com.seucontrolefinanceiro.model.PaymentCategory;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PaymentCategoryDTO {

    private String id;
    @NonNull
    private String description;
    @NonNull
    private String billType;

    public PaymentCategoryDTO(PaymentCategory pc) {
        this.id = pc.getId();
        this.description = pc.getDescription();
        this.billType = pc.getBillType().getDescription();
    }

    public static List<PaymentCategoryDTO> converter(List<PaymentCategory> paymentCategories) {
        return paymentCategories.stream().map(PaymentCategoryDTO::new).collect(Collectors.toList());
    }
}
