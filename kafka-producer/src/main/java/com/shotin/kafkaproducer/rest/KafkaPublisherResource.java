package com.shotin.kafkaproducer.rest;

import com.shotin.kafkaproducer.kafka.BankAccountPublisher;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.BankAccountList;
import com.shotin.kafkaproducer.model.BankAccountSchedulerConfig;
import com.shotin.kafkaproducer.scheduler.BankAccountsScheduler;
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
public class KafkaPublisherResource {

    private final Logger LOG = LoggerFactory.getLogger(KafkaPublisherResource.class);

    private BankAccountPublisher bankAccountPublisher;
    private BankAccountGenerator bankAccountGenerator;
    private BankAccountTypeEnricher bankAccountTypeEnricher;
    private BankAccountsScheduler bankAccountsScheduler;

    public KafkaPublisherResource(@Autowired BankAccountPublisher bankAccountPublisher,
                                  @Autowired BankAccountGenerator bankAccountGenerator,
                                  @Autowired BankAccountTypeEnricher bankAccountTypeEnricher,
                                  @Autowired BankAccountsScheduler bankAccountsScheduler) {
        this.bankAccountPublisher = bankAccountPublisher;
        this.bankAccountGenerator = bankAccountGenerator;
        this.bankAccountTypeEnricher = bankAccountTypeEnricher;
        this.bankAccountsScheduler = bankAccountsScheduler;
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

    @PostMapping("/bankAccounts/manyRandom/{count}")
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

    @PatchMapping("/bankAccountScheduler")
    public ResponseEntity<Object> updateBankAccountScheduler(@RequestBody BankAccountSchedulerConfig config) {
        bankAccountsScheduler.applyConfiguration(config);
        return ResponseEntity.ok().build();
    }

}
