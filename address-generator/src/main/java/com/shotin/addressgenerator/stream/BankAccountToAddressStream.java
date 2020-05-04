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

@EnableBinding(BankAccountTableProcessor.class)
public class BankAccountToAddressStream {
    private final Logger LOG = LoggerFactory.getLogger(BankAccountToAddressStream.class);

    private BankAccountToAddressTransformer bankAccountToAddressTransformer;

    public BankAccountToAddressStream(@Autowired BankAccountToAddressTransformer bankAccountToAddressTransformer) {
        this.bankAccountToAddressTransformer = bankAccountToAddressTransformer;
    }

    @StreamListener
    @SendTo("addresses-output")
    public KStream<String, Address> generateAddressFromBankAccount(
            @Input("bank-accounts-input") KTable<String, BankAccount> bankAccountTable) {
        return bankAccountTable
                .mapValues((key, bankAccount) -> {
                    LOG.info("received bank account key="+key);
                    return bankAccount;
                })
//                .filter((key, bankAccount) -> bankAccount.getLastName().length() > 3)
                 .filter((key, bankAccount) -> bankAccount.getLastName().startsWith("А"))
                .mapValues((key, bankAccount) -> bankAccountToAddressTransformer.transform(key, bankAccount))
                .toStream();

    }
}
