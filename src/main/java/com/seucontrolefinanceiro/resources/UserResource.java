package com.seucontrolefinanceiro.resources;

import com.seucontrolefinanceiro.model.Bill;
import com.seucontrolefinanceiro.model.User;
import com.seucontrolefinanceiro.dto.UserDTO;
import com.seucontrolefinanceiro.form.UserForm;
import com.seucontrolefinanceiro.services.BillService;
import com.seucontrolefinanceiro.services.UserService;
import com.seucontrolefinanceiro.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("scf-service/users")
public class UserResource implements Resource<UserDTO, UserForm> {

    @Autowired private UserService service;
    @Autowired private BillService billService;

    @Override
    @GetMapping
    public ResponseEntity<List<UserDTO>> find(String user) {
        System.out.println(user);
        List<User> users = service.findAll();
        return ResponseEntity.ok().body(UserDTO.converter((users)));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        User user = service.findById(id);
        return ResponseEntity.ok().body(new UserDTO(user));
    }

    @Override
    @PostMapping
    public ResponseEntity<UserDTO> insert(@RequestBody @Validated UserForm form, UriComponentsBuilder uriBuilder) {
        User user = form.converter();
        user = service.save(user);
        URI uri = uriBuilder.path("scf-service/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDTO(user));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/resetAccount/{id}")
    public ResponseEntity<String> resetAccount(@PathVariable String id) {
        try {
        User user = service.findById(id);
        billService.deleteAll(user.getBills());
        return ResponseEntity.ok("Account reset successfully!");
        } catch (ObjectNotFoundException e) {
           return ResponseEntity.notFound().build();
        }
    }

    @Override
    @PutMapping
    public ResponseEntity<UserDTO> update(@RequestBody @Validated UserForm form, UriComponentsBuilder uriBuilder) {
        User user = form.converter();
        user = service.update(user);
        URI uri = uriBuilder.path("scf-service/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{user}/bills")
    public ResponseEntity<List<Bill>> findBills(@PathVariable String user) {
        User userFound = service.findByEmail(user).get();
        return ResponseEntity.ok().body(userFound.getBills());
    }
}
