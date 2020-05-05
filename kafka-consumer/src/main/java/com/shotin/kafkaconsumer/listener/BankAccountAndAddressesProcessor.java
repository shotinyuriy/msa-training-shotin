package com.shotin.kafkaconsumer.listener;


import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.kafkaconsumer.model.BankAccountInfo;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface BankAccountAndAddressesProcessor {

    String BANK_ACCOUNTS_INPUT = "bank-accounts-input";
    String ADDRESSES_INPUT = "addresses-input";
    String BANK_ACCOUNT_INFOS_OUTPUT = "bank-account-infos-output";
    String BANK_ACCOUNT_INFOS_INPUT = "bank-account-infos-input";

    @Input(BANK_ACCOUNTS_INPUT)
    KTable<String, BankAccount> bankAccountInput();

    @Input(ADDRESSES_INPUT)
    KTable<String, Address> addressesInput();

    @Output(BANK_ACCOUNT_INFOS_OUTPUT)
    KStream<String, JoinedBankAccountInfo> bankAccountInfosOutput();

    @Input(BANK_ACCOUNT_INFOS_INPUT)
    KTable<String, JoinedBankAccountInfo> bankAccountInfosInput();
}
