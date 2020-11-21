package com.seucontrolefinanceiro.form;

import com.seucontrolefinanceiro.model.BillType;
import com.seucontrolefinanceiro.model.PaymentCategory;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Getter
@RequiredArgsConstructor
public class PaymentCategoryForm {

    @Id
    private String id;
    @NonNull
    private String description;
    @NonNull
    private boolean mutable;
    @NonNull
    private BillType billType;

    public PaymentCategory converter() {
        return PaymentCategory.builder()
                .id(this.id)
                .description(this.description)
                .billType(this.billType)
                .build();
    }
}
