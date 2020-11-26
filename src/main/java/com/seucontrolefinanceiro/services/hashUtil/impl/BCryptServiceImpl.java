package com.seucontrolefinanceiro.services.hashUtil.impl;

import com.seucontrolefinanceiro.services.hashUtil.HashService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptServiceImpl implements HashService {

    @Override
    public String generateHash(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
