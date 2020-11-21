package com.seucontrolefinanceiro.services;

import com.seucontrolefinanceiro.model.User;
import com.seucontrolefinanceiro.repository.UserRepository;
import com.seucontrolefinanceiro.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class UserService implements Service<User> {

    @Autowired
    private UserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(String id) {
        Optional<User> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Object not found!"));
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public void delete(String id) {
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public User update(User newUser) {
        User currentUser = findById(newUser.getId());
        currentUser = updateData(newUser, currentUser.getId());
        return repository.save(currentUser);
    }

    @Override
    public User updateData(User newUser, String id) {
        return User.builder()
                .id(id)
                .fullName(newUser.getFullName())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .cpf(newUser.getCpf())
                .build();
    }

    public void deleteAll(List<User> users) {
        repository.deleteAll(users);
    }
}
