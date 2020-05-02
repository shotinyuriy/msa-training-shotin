package com.shotin.kafkaproducer.scheduler;


import com.shotin.kafkaproducer.kafka.BankAccountPublisher;
import com.shotin.kafkaproducer.model.BankAccountList;
import com.shotin.kafkaproducer.service.BankAccountGenerator;
import com.shotin.kafkaproducer.service.BankAccountTypeEnricher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class BankAccountsScheduler {

    private final Logger LOG = LoggerFactory.getLogger(BankAccountsScheduler.class);

    @Value("${bank-account-scheduler.batch-size}")
    private int batchSize;

    private BankAccountGenerator bankAccountGenerator;
    private BankAccountPublisher bankAccountPublisher;
    private BankAccountTypeEnricher bankAccountTypeEnricher;

    public BankAccountsScheduler(@Autowired BankAccountGenerator bankAccountGenerator,
                                 @Autowired BankAccountPublisher bankAccountPublisher,
                                 @Autowired BankAccountTypeEnricher bankAccountTypeEnricher) {
        this.bankAccountGenerator = bankAccountGenerator;
        this.bankAccountPublisher = bankAccountPublisher;
        this.bankAccountTypeEnricher = bankAccountTypeEnricher;
    }

    @Scheduled(initialDelay = 1000L, fixedRate = 5000L)
    public void generateAndPublishToKafka() {
        LOG.info("Started generateAndPublishToKafka");
        Flux<BankAccountList> bankAccountFlux = bankAccountGenerator.getManyRandomAccountsNonBlocking(batchSize);
        bankAccountFlux.subscribe(bankAccountList -> {
            bankAccountList.getBankAccounts().stream()
                    .map(bankAccount -> bankAccountTypeEnricher.enrich(bankAccount))
                    .forEach(bankAccount -> bankAccountPublisher.sendBankAccount(bankAccount));
        });
        LOG.info("Finished generateAndPublishToKafka");
    }
}
