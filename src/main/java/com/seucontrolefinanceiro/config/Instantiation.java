package com.seucontrolefinanceiro.config;

import com.seucontrolefinanceiro.model.BillType;
import com.seucontrolefinanceiro.model.PaymentCategory;
import com.seucontrolefinanceiro.model.User;
import com.seucontrolefinanceiro.repository.BillRepository;
import com.seucontrolefinanceiro.repository.PaymentCategoryRepository;
import com.seucontrolefinanceiro.repository.UserRepository;
import com.seucontrolefinanceiro.services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class Instantiation implements CommandLineRunner {

    @Autowired private PaymentCategoryRepository paymentCategoryRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private BillService billService;

    @Autowired private BillRepository billRepository;

    @Override
    public void run(String... args) throws Exception {

        boolean notExistData = paymentCategoryRepository.findAll().isEmpty();

        if (notExistData) {
            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Empréstimo")
                    .billType(BillType.RECEIVEMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Salário")
                    .billType(BillType.RECEIVEMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Investimento")
                    .billType(BillType.RECEIVEMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Outros")
                    .billType(BillType.RECEIVEMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Banco")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Alimentação")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Saúde")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Serviços")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Lanchonetes")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Compras")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Dívidas")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Educação")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Impostos")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Lazer")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Supermercado")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Presentes")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Roupas")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Transporte")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Viagem")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            paymentCategoryRepository.save(
                PaymentCategory.builder()
                    .description("Outros")
                    .billType(BillType.PAYMENT)
                    .build()
            );

            userRepository.save(
                User.builder()
                    .id("5edc8081336c5266fcc81dd5")
                    .fullName("Administrador")
                    .email("admin@admin.com.br")
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .cpf("45073070828")
                    .build()
            );

            userRepository.save(
                User.builder()
                    .fullName("Cristhian Dias")
                    .email("heycristhian@gmail.com")
                    .password(new BCryptPasswordEncoder().encode("admin"))
                    .cpf("45073070828")
                    .build()
            );
        }


    }
}
