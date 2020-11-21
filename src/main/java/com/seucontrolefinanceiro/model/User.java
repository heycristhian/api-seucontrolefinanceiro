package com.seucontrolefinanceiro.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Builder
@Document()
@EqualsAndHashCode(of = {"fullName", "user"})
@ToString(of = {"fullName", "bills"})
@AllArgsConstructor(staticName = "of")
public class User implements UserDetails {

    @Id
    @Getter
    private String id;
    @Getter
    @NotNull
    private String fullName;
    @Getter
    private String email;
    @Getter
    private String password;
    @Getter
    private String cpf;

    @DBRef(lazy = false)
    private List<Profile> profiles = new ArrayList<>();

    @DBRef(lazy = true)
    @Builder.Default private List<Bill> bills;

    @Deprecated
    User() {}

    public static class UserBuilder {

        public User build() {
            User user = User.of(this.id, this.fullName, this.email, this.password, this.cpf, new ArrayList<>(), new ArrayList<>());

            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

            Set<ConstraintViolation<User>> violations = validator.validate(user);

            if (!violations.isEmpty()) {
                String message = violations.stream().map(v -> String.format("%s: %s", v.getPropertyPath(), v.getMessage())).collect(Collectors.joining("\n"));
                throw new RuntimeException(message);
            }
            return user;
        }
    }

    public List<Bill> getBills () {
        return Collections.unmodifiableList(this.bills);
    }

    public void addToListBill(Bill bill) {
        if (this.bills.size() > 0) {
            this.bills.stream().filter(x -> x.getId().equals(bill.getId()))
                    .findAny().ifPresent((value) -> {
                    return;
            });
        }
        this.bills.add(bill);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.profiles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
