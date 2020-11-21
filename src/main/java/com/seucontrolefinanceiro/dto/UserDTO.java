package com.seucontrolefinanceiro.dto;

import com.seucontrolefinanceiro.model.Bill;
import com.seucontrolefinanceiro.model.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDTO {

        private final String id;
        private final String fullName;
        private final String email;
        private final String cpf;
        private List<Bill> bills = new ArrayList<>();

        public UserDTO(User user) {
            this.id = user.getId();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
            this.cpf = user.getCpf();
            this.bills = user.getBills();
        }

        public static List<UserDTO> converter(List<User> users) {
            return users.stream().map(UserDTO::new).collect(Collectors.toList());
        }
}
