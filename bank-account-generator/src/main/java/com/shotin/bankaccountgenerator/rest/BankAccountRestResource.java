package com.shotin.bankaccountgenerator.rest;

import com.shotin.bankaccountgenerator.generator.BankAccountGenerator;
import com.shotin.bankaccountgenerator.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
