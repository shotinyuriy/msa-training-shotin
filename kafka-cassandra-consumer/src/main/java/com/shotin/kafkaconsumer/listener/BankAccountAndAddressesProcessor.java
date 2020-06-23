package com.shotin.kafkaconsumer.listener;


import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Profile("1-step")
public interface BankAccountAndAddressesProcessor {
    String BANK_ACCOUNTS_INPUT = "bank-accounts-input";
    String ADDRESSES_INPUT = "addresses-input";

    @Input(BANK_ACCOUNTS_INPUT)
    KTable<UUID, BankAccount> bankAccountInput();

    @Input(ADDRESSES_INPUT)
    KTable<UUID, Address> addressesInput();
}
