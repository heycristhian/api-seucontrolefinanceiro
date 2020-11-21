package com.seucontrolefinanceiro.dto;

import lombok.Getter;

@Getter
public class TokenDTO {

    private String token;
    private String type;
    private String userId;

    public TokenDTO(String token, String bearer, String userId) {
        this.token = token;
        this.type = bearer;
        this.userId = userId;
    }
}
