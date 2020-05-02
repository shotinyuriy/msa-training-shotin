package com.shotin.kafkaproducer.rest;

import com.shotin.kafkaproducer.kafka.BankAccountPublisher;
import com.shotin.kafkaproducer.model.BankAccount;
import com.shotin.kafkaproducer.model.BankAccountList;
import com.shotin.kafkaproducer.service.BankAccountGenerator;
import com.shotin.kafkaproducer.service.BankAccountTypeEnricher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/kafka")
public class KafkaMessageResource {

    private final Logger LOG = LoggerFactory.getLogger(KafkaMessageResource.class);

    private BankAccountPublisher bankAccountPublisher;
    private BankAccountGenerator bankAccountGenerator;
    private BankAccountTypeEnricher bankAccountTypeEnricher;

    public KafkaMessageResource(@Autowired BankAccountPublisher bankAccountPublisher,
                                @Autowired BankAccountGenerator bankAccountGenerator,
                                @Autowired BankAccountTypeEnricher bankAccountTypeEnricher) {
        this.bankAccountPublisher = bankAccountPublisher;
        this.bankAccountGenerator = bankAccountGenerator;
        this.bankAccountTypeEnricher = bankAccountTypeEnricher;
    }


    @PostMapping("/bankAccounts")
    public ResponseEntity<Object> sendBankAccount(@RequestBody BankAccount bankAccount) {

        if(bankAccount == null) {
            return ResponseEntity.badRequest().build();
        }

        bankAccountPublisher.sendBankAccount(bankAccount);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/bankAccounts/random")
    public Flux<BankAccount> sendRandomBankAccount() {
        Flux<BankAccount> bankAccountFlux = bankAccountGenerator.getRandomAccountNonBlocking();
        bankAccountFlux.subscribe(bankAccount -> {
            LOG.info("Retrieved Bank Account "+bankAccount);
            bankAccount = bankAccountTypeEnricher.enrich(bankAccount);
            LOG.info("Enriched Bank Account "+bankAccount);
            bankAccountPublisher.sendBankAccount(bankAccount);
        });
        return bankAccountFlux;
    }
    @PostMapping("/bankAccounts/randomCount/{count}")
    public Flux<BankAccountList> sendRandomBankAccount(@PathVariable int count) {
        Flux<BankAccountList> bankAccountFlux = bankAccountGenerator.getManyRandomAccountsNonBlocking(count);
        bankAccountFlux.subscribe(bankAccountList -> {
            LOG.info("Retrieved Bank Account "+bankAccountList);
            bankAccountList.getBankAccounts().stream()
                    .map(bankAccount -> bankAccountTypeEnricher.enrich(bankAccount))
                    .forEach(bankAccount -> bankAccountPublisher.sendBankAccount(bankAccount));
            LOG.info("Enriched Bank Accounts "+bankAccountList);
        });
        return bankAccountFlux;
    }

}
