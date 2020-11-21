package com.seucontrolefinanceiro.resources;

import com.seucontrolefinanceiro.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface Resource<T, K> {

    public ResponseEntity<List<T>> find(String query);
    public ResponseEntity<T> findById(@PathVariable String id);
    public ResponseEntity<T> insert(@RequestBody @Validated K form, UriComponentsBuilder uriBuilder);
    public ResponseEntity<Void> delete(@PathVariable String id);
    public ResponseEntity<UserDTO> update(@RequestBody @Validated K form, UriComponentsBuilder uriBuilder);
}
