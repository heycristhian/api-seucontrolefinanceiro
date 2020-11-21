package com.seucontrolefinanceiro.model;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document
@Getter
public class Profile implements GrantedAuthority {

    private String id;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
