package com.shotin.addressgenerator.transformer;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;

public interface BankAccountToAddressTransformer {
    Address transform(String key, BankAccount bankAccount);
}
