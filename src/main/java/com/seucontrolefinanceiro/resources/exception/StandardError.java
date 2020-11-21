package com.seucontrolefinanceiro.resources.exception;

import lombok.*;

@Builder
@Getter
@Setter
public class StandardError {

    private final Long timestamp;
    private final Integer status;
    private final String error;
    private final String message;
    private final String path;
}
