package com.shotin.addressgenerator.stream;

import com.shotin.addressgenerator.transformer.BankAccountToAddressTransformer;
import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.UUID;

import static com.shotin.addressgenerator.stream.BankAccountTableProcessor.ADDRESSES_OUTPUT;
import static com.shotin.addressgenerator.stream.BankAccountTableProcessor.BANK_ACCOUNTS_INPUT;

@EnableBinding(BankAccountTableProcessor.class)
public class BankAccountToAddressStream {
    private final Logger LOG = LoggerFactory.getLogger(BankAccountToAddressStream.class);

    private BankAccountToAddressTransformer bankAccountToAddressTransformer;

    public BankAccountToAddressStream(@Autowired BankAccountToAddressTransformer bankAccountToAddressTransformer) {
        this.bankAccountToAddressTransformer = bankAccountToAddressTransformer;
    }

    @StreamListener
    @SendTo(ADDRESSES_OUTPUT)
    public KStream<UUID, Address> generateAddressFromBankAccount(
            @Input(BANK_ACCOUNTS_INPUT) KTable<UUID, BankAccount> bankAccountTable) {
        return bankAccountTable
                .mapValues((key, bankAccount) -> {
                    LOG.info("received bank account key=" + key);
                    return bankAccount;
                })
                .filter((key, bankAccount) -> bankAccount.getLastName().startsWith("А"))
                .mapValues((key, bankAccount) -> bankAccountToAddressTransformer.transform(key, bankAccount))
                .toStream();

    }
}
