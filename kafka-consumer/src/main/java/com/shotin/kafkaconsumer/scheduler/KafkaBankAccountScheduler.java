package com.shotin.kafkaconsumer.scheduler;

import com.shotin.kafkaconsumer.model.AddressEntity;
import com.shotin.kafkaconsumer.model.BankAccountEntity;
import com.shotin.kafkaconsumer.model.BankAccountInfo;
import com.shotin.kafkaconsumer.repository.BankAccountQueue;
import com.shotin.kafkaconsumer.repository.BankAccountRepository;
import com.shotin.bankaccount.model.kafka.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class KafkaBankAccountScheduler {

    private final Logger LOG = LoggerFactory.getLogger(KafkaBankAccountScheduler.class);

    private BankAccountQueue bankAccountQueue;
    private BankAccountRepository bankAccountRepository;

    public KafkaBankAccountScheduler(@Autowired BankAccountQueue bankAccountQueue,
                                     @Autowired BankAccountRepository bankAccountRepository) {
        this.bankAccountQueue = bankAccountQueue;
        this.bankAccountRepository = bankAccountRepository;
    }

//    @Scheduled(initialDelay = 1000L, fixedDelay = 10000L)
    public void saveBankAccountsToDatabase() {
        int currentSize = bankAccountQueue.size();
        LOG.info("Staring to save bank accounts to DB accounts count = "+currentSize);
        for(int i = 0; i < currentSize; i++) {
            BankAccount bankAccount = bankAccountQueue.pop();
            if(bankAccount == null) {
                break;
            }
            BankAccountEntity bankAccountEntity = new BankAccountEntity(bankAccount);
            BankAccountInfo bankAccountInfo = new BankAccountInfo(bankAccountEntity.getUuid(), bankAccountEntity, new AddressEntity());
            bankAccountRepository.save(bankAccountInfo);
        }
        LOG.info("Finished to save bank accounts to DB accounts count = "+currentSize);
    }
}
