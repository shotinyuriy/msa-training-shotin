package com.shotin.addressgenerator.stream;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface BankAccountTableProcessor {

    String BANK_ACCOUNTS_INPUT = "bank-accounts-input";
    String ADDRESSES_OUTPUT = "addresses-output";

    @Input(BANK_ACCOUNTS_INPUT)
    KTable<String, BankAccount> inputTable();

    @Output(ADDRESSES_OUTPUT)
    KStream<String, Address> outputTable();
}
