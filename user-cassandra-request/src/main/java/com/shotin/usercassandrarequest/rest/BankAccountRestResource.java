package com.shotin.usercassandrarequest.rest;

import com.shotin.usercassandrarequest.model.BankAccountEntity;
import com.shotin.usercassandrarequest.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountRestResource {

    private BankAccountRepository bankAccountRepository;

    public BankAccountRestResource(@Autowired BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Object> getBankAccountByUuid(@PathVariable UUID uuid) {
        Optional<BankAccountEntity> bankAccount = bankAccountRepository.findById(uuid);
        if(!bankAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bankAccount.get());
    }
}
