package com.shotin.addressgenerator.stream;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface BankAccountTableProcessor {
    @Input("bank-accounts-input")
    KTable<String, BankAccount> inputTable();

    @Output("addresses-output")
    KStream<String, Address> outputTable();
}
