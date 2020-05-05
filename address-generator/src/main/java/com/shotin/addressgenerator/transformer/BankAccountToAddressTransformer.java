package com.shotin.addressgenerator.transformer;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;

import java.util.UUID;

public interface BankAccountToAddressTransformer {
    Address transform(UUID key, BankAccount bankAccount);
}
