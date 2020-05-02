package com.shotin.bankaccountgenerator.rest;

import com.shotin.bankaccountgenerator.generator.BankAccountGenerator;
import com.shotin.bankaccountgenerator.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bankAccounts")
public class BankAccountRestResource {

    private BankAccountGenerator bankAccountGenerator;

    public BankAccountRestResource(@Autowired BankAccountGenerator bankAccountGenerator) {
        this.bankAccountGenerator = bankAccountGenerator;
    }

    @GetMapping("/random")
    public ResponseEntity<BankAccount> getRandomBankAccount() {
        BankAccount bankAccount = bankAccountGenerator.generateBankAccount();

        ResponseEntity<BankAccount> responseEntity = ResponseEntity.ok(bankAccount);
        return responseEntity;
    }

    @GetMapping("/randomCount/{count}")
    public ResponseEntity<Object> getRandomBankAccount(@PathVariable int count) {
        if (count < 1) {
            return ResponseEntity.badRequest().body("count should be an integer greater than zero");
        }
        List<BankAccount> bankAccounts = new ArrayList<>(count);
        for(int i = 0; i < count; i++) {
            BankAccount bankAccount = bankAccountGenerator.generateBankAccount();
            bankAccounts.add(bankAccount);
        }

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(bankAccounts);
        return responseEntity;
    }
}
