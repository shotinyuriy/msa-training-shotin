package com.shotin.consumer.redis.stream;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import javax.security.auth.kerberos.KeyTab;
import java.util.UUID;

public interface BankAccountAndAddressProcessor {

    String BANK_ACCOUNTS_INPUT = "bank-accounts-input";
    String ADDRESSES_INPUT = "addresses-input";
    String BANK_ACCOUNT_INFOS_OUTPUT = "bank-account-infos-output";
    String BANK_ACCOUNT_INFOS_INPUT = "bank-account-infos-input";

    @Input(BANK_ACCOUNTS_INPUT)
    KTable<UUID, BankAccount> bankAccountsInput();

    @Input(ADDRESSES_INPUT)
    KTable<UUID, Address> addressesInput();

    @Output(BANK_ACCOUNT_INFOS_OUTPUT)
    KStream<UUID, JoinedBankAccountInfo> bankAccountInfoOutput();

//    @Input(BANK_ACCOUNT_INFOS_INPUT)
//    KStream<UUID, JoinedBankAccountInfo> bankAccountInfoInput();
}
