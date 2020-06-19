package com.shotin.consumer.redis.repository;

import com.shotin.consumer.redis.model.AddressEntity;
import com.shotin.consumer.redis.model.BankAccountEntity;
import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

@ActiveProfiles("redis-repository-test")
@SpringBootTest
public class BankAccountInfoRepositoryTest {

    private Logger LOG = LoggerFactory.getLogger(BankAccountInfoRepositoryTest.class);

    @Autowired
    private BankAccountInfoRepository bankAccountInfoRepository;

    @Autowired
    private SimpleReactiveBankAccountInfoRepository reactiveBankAccountInfoRepository;

    @Test
    public void testCrud() {
        UUID uuid = UUID.randomUUID();
        BankAccountInfoEntity bankAccountInfoEntity = createBankAccountInfo(uuid, "-one");

        UUID uuid2 = UUID.randomUUID();
        BankAccountInfoEntity bankAccountInfoEntity2 = createBankAccountInfo(uuid2, "-two");
        // CREATE
        BankAccountInfoEntity savedBankAccountInfoEntity = bankAccountInfoRepository.save(bankAccountInfoEntity);
        Assertions.assertNotNull(savedBankAccountInfoEntity);

        BankAccountInfoEntity savedBankAccountInfoEntity2 = bankAccountInfoRepository.save(bankAccountInfoEntity2);
        Assertions.assertNotNull(savedBankAccountInfoEntity2);
        // READ
        Optional<BankAccountInfoEntity> foundBankAccountInfo = bankAccountInfoRepository.findById(uuid);
        Assertions.assertTrue(foundBankAccountInfo.isPresent());
        // UPDATE
        foundBankAccountInfo.get().getBankAccount().setLastName("New Lastname");
        foundBankAccountInfo.get().getAddress().setStreet("New Street");
        BankAccountInfoEntity updatedBankAccountInfoEntity = bankAccountInfoRepository.save(foundBankAccountInfo.get());
        Assertions.assertNotNull(updatedBankAccountInfoEntity);

        // DELETE
        bankAccountInfoRepository.delete(savedBankAccountInfoEntity);
    }

    @Test
    public void testReactiveCrud() {
        UUID uuid = UUID.randomUUID();
        BankAccountInfoEntity bankAccountInfoEntity = createBankAccountInfo(uuid, "-one");

        UUID uuid2 = UUID.randomUUID();
        BankAccountInfoEntity bankAccountInfoEntity2 = createBankAccountInfo(uuid2, "-two");

        BankAccountInfoEntity bankAccountInfoEntity3 = createBankAccountInfo(null, "-three");

        reactiveBankAccountInfoRepository.save(bankAccountInfoEntity)
                .doOnError(e -> LOG.error(e, () -> "SAVE 1 FAILED"))
                .subscribe(result -> LOG.info(() -> "SAVE 1 SUCCESS "+result));

        reactiveBankAccountInfoRepository.save(bankAccountInfoEntity2)
                .doOnError(e -> LOG.error(e, () -> "SAVE 2 FAILED"))
                .subscribe(result -> LOG.info(() -> "SAVE 2 SUCCESS "+result));

        reactiveBankAccountInfoRepository.save(bankAccountInfoEntity3)
                .doOnError(e -> LOG.error(e, () -> "SAVE 3 FAILED"))
                .subscribe(result -> LOG.info(() -> "SAVE 3 SUCCESS "+result));

        BankAccountInfoEntity foundRxBankAccountInfo = reactiveBankAccountInfoRepository.findById(uuid).block();

        reactiveBankAccountInfoRepository.findAll().toIterable()
                .forEach(bankAccountInfo -> LOG.info(() -> "REACTIVE BANK ACCOUNT INFO: "+bankAccountInfo));

        reactiveBankAccountInfoRepository.count()
                .subscribe(count -> LOG.info(() -> "COUNT = "+count));

        reactiveBankAccountInfoRepository
                .deleteById(uuid)
                .doOnSuccess(result -> LOG.info(()->"DO DELETED 1 "+result))
                .subscribe(result -> LOG.info(()->"SUB DELETED 1 "+result));

        reactiveBankAccountInfoRepository
                .deleteById(uuid2)
                .doOnSuccess(result -> LOG.info(()->"DO DELETED 2 "+result))
                .subscribe(result -> LOG.info(()->"SUB DELETED 2 "+result));
    }

    private BankAccountInfoEntity createBankAccountInfo(UUID uuid, String suffix) {
        BankAccountEntity bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setLastName("Lastname"+suffix);
        bankAccountEntity.setFirstName("Firstname"+suffix);
        bankAccountEntity.setPatronymic("Patronymic"+suffix);

        bankAccountEntity.setUuid(uuid);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setStreet("Street"+suffix);
        addressEntity.setCity("City"+suffix);
        addressEntity.setState("State"+suffix);

        BankAccountInfoEntity bankAccountInfoEntity = new BankAccountInfoEntity(uuid, bankAccountEntity, addressEntity);

        return bankAccountInfoEntity;
    }
}
