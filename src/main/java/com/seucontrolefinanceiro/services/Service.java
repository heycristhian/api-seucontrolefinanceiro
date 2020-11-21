package com.seucontrolefinanceiro.services;

import com.seucontrolefinanceiro.repository.UserRepository;

import java.util.List;

public interface Service<T> {

    UserRepository repository = null;

    public List<T> findAll();
    public T findById(String id);
    public T save(T t);
    public void delete(String id);
    public T update(T newObj);
    public T updateData(T newObj, String id);
}
