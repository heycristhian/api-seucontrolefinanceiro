package com.seucontrolefinanceiro.config.security;

import com.seucontrolefinanceiro.model.User;
import com.seucontrolefinanceiro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byUser = userRepository.findByEmail(email);
        if (byUser.isPresent()) {
            return byUser.get();
        }
        throw new UsernameNotFoundException("Dados inválidos!");
    }
}