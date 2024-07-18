package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    public ResponseEntity<Account> registration(@RequestBody Account account) {
        if(account.getUsername() == null || account.getUsername().isEmpty() || account.getPassword().length() < 4) {
            return ResponseEntity.badRequest().build();
        }
        if(accountRepository.findByUsername(account.getUsername()).isPresent()) {
            return ResponseEntity.status(409).build();
        }
        try {
            Account newAccount = accountRepository.save(account);
            return ResponseEntity.ok().body(newAccount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }
    public ResponseEntity<Account> login(Account loginRequest) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(loginRequest.getUsername());

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.ok().body(account);
            }
        }

        return ResponseEntity.status(401).build(); // Unauthorized
    }

}
