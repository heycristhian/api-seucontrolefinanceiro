package com.seucontrolefinanceiro.repository;

import com.seucontrolefinanceiro.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findByCpfContainingIgnoreCase(String cpf);

    Optional<User> findByEmail(String email);

}
