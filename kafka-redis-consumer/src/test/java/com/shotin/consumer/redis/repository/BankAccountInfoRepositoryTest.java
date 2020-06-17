package com.shotin.consumer.redis.repository;

import com.shotin.consumer.redis.model.AddressEntity;
import com.shotin.consumer.redis.model.BankAccountEntity;
import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

@ActiveProfiles("test")
@SpringBootTest
public class BankAccountInfoRepositoryTest {

    @Autowired
    private BankAccountInfoRepository bankAccountInfoRepository;

    @Autowired
    private ReactiveBankAccountInfoRepository reactiveBankAccountInfoRepository;

//    @Test
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

        reactiveBankAccountInfoRepository.save(bankAccountInfoEntity)
                .doOnError(e -> e.printStackTrace())
                .subscribe(System.out::println);

        reactiveBankAccountInfoRepository.save(bankAccountInfoEntity2)
                .doOnError(e -> e.printStackTrace())
                .subscribe(System.out::println);

        BankAccountInfoEntity foundRxBankAccountInfo = reactiveBankAccountInfoRepository.findById(uuid).block();

        reactiveBankAccountInfoRepository.findAll().toIterable()
                .forEach(bankAccountInfo -> System.out.println("REACTIVE BANK ACCOUNT INFO: "+bankAccountInfo));

        reactiveBankAccountInfoRepository.delete(uuid).subscribe(System.out::println);
        reactiveBankAccountInfoRepository.delete(uuid2).subscribe(System.out::println);
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
