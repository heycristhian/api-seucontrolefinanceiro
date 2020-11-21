package com.seucontrolefinanceiro.resources;

import com.seucontrolefinanceiro.config.security.TokenService;
import com.seucontrolefinanceiro.model.User;
import com.seucontrolefinanceiro.dto.TokenDTO;
import com.seucontrolefinanceiro.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDTO> authenticate(@RequestBody @Valid LoginForm form) {
        UsernamePasswordAuthenticationToken dataLogin = form.converter();
        try {
            Authentication authenticate = authenticationManager.authenticate(dataLogin);
            String token = tokenService.generateToken(authenticate);
            User loggedUser = (User) authenticate.getPrincipal();
            return ResponseEntity.ok(new TokenDTO(token, "Bearer", loggedUser.getId()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
