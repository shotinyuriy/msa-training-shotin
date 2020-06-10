package com.shotin.bankaccountgenerator.generator;

import com.shotin.bankaccountgenerator.model.BankAccount;
import com.shotin.bankaccountgenerator.model.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BankAccountGenerator {

    private Map<Gender, NamesGenerator> nameGenerators;

    private Random random = new Random(System.nanoTime());

    private AtomicLong accountNumberSeed = new AtomicLong(System.currentTimeMillis());

    public BankAccountGenerator(@Autowired NamesGenerator manNamesGenerator,
                                @Autowired NamesGenerator womanNamesGenerator) {
        this.nameGenerators = new HashMap<>();
        this.nameGenerators.put(Gender.MALE, manNamesGenerator);
        this.nameGenerators.put(Gender.FEMALE, womanNamesGenerator);
    }

    public BankAccount generateBankAccount() {
        NamesGenerator namesGenerator = nameGenerators.get(getRandomGender());

        BankAccount bankAccount = new BankAccount();

        bankAccount.setUuid(UUID.randomUUID());
        bankAccount.setFirstName(namesGenerator.getRandomFirstName());
        bankAccount.setLastName(namesGenerator.getRandomLastName());
        bankAccount.setPatronymic(namesGenerator.getRandomPatronymic());
        bankAccount.setAccountNumber(accountNumberSeed.getAndIncrement());

        return bankAccount;
    }

    protected  Gender getRandomGender() {
        int rnd = random.nextInt(100);
        if (rnd % 2 == 0) {
            return Gender.MALE;
        } else {
            return Gender.FEMALE;
        }
    }
}
