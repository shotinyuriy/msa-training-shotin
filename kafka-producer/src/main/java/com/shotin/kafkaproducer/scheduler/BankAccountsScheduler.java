package com.shotin.kafkaproducer.scheduler;


import com.shotin.kafkaproducer.kafka.AsyncBankAccountPublisher;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.BankAccountList;
import com.shotin.kafkaproducer.model.BankAccountSchedulerConfig;
import com.shotin.kafkaproducer.service.BankAccountGenerator;
import com.shotin.kafkaproducer.service.BankAccountTypeEnricher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class BankAccountsScheduler {

    private final Logger LOG = LoggerFactory.getLogger(BankAccountsScheduler.class);

    @Value("${bank-account-scheduler.batch-size}")
    private volatile int batchSize;

    private volatile boolean enabled;

    private Pattern lastnameFilterRegexp = null;
    private Predicate<BankAccount> lastnameFilterByRegexp = null;

    private BankAccountGenerator bankAccountGenerator;
    private AsyncBankAccountPublisher asyncBankAccountPublisher;
    private BankAccountTypeEnricher bankAccountTypeEnricher;

    public BankAccountsScheduler(@Autowired BankAccountGenerator bankAccountGenerator,
                                 @Autowired AsyncBankAccountPublisher asyncBankAccountPublisher,
                                 @Autowired BankAccountTypeEnricher bankAccountTypeEnricher) {
        this.bankAccountGenerator = bankAccountGenerator;
        this.asyncBankAccountPublisher = asyncBankAccountPublisher;
        this.bankAccountTypeEnricher = bankAccountTypeEnricher;
        enabled = true;
        initializeLastnameFilterByRegexp();
    }

    public void applyConfiguration(BankAccountSchedulerConfig config) {
        if(config.getEnabled() != null) {
            enabled = config.getEnabled();
        }
        if (config.getBatchSize() != null) {
            batchSize = config.getBatchSize();
        }
        this.setLastnameFilterRegexp(config.getLastnameRegExp());
    }

    public void pause() {
        enabled = false;
    }

    public void start() {
        enabled = true;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public synchronized void setLastnameFilterRegexp(String regexp) {
        if (regexp == null) {
            lastnameFilterRegexp = null;
        } else {
            lastnameFilterRegexp = Pattern.compile(regexp);
        }
        initializeLastnameFilterByRegexp();
    }

    protected synchronized void initializeLastnameFilterByRegexp() {
        lastnameFilterByRegexp  = bankAccount -> {
            if(lastnameFilterRegexp == null) {
                return true;
            }
            return lastnameFilterRegexp.matcher(bankAccount.getLastName()).matches();
        };
        LOG.info("Initialized Lastname RegExp="+lastnameFilterRegexp);
    }

    @Scheduled(initialDelay = 1000L, fixedRate = 10000L)
    public void generateAndPublishToKafka() {
        if (enabled) {
            LOG.info("Started generateAndPublishToKafka");
            Flux<BankAccountList> bankAccountFlux = bankAccountGenerator.getManyRandomAccountsNonBlocking(batchSize);
            bankAccountFlux.subscribe(bankAccountList -> {
                bankAccountList.getBankAccounts().stream()
                        .filter(lastnameFilterByRegexp)
                        .map(bankAccount -> bankAccountTypeEnricher.enrich(bankAccount))
                        .forEach(bankAccount -> asyncBankAccountPublisher.sendBankAccount(bankAccount));
            });
            LOG.info("Finished generateAndPublishToKafka");
        } else {
            LOG.info("generateAndPublishToKafka is disabled");
        }
    }
}
