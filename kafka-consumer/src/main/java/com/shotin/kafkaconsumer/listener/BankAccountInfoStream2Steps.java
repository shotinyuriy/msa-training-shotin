package com.shotin.kafkaconsumer.listener;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.kafkaconsumer.converter.BankAccountInfoConverter;
import com.shotin.kafkaconsumer.model.AddressEntity;
import com.shotin.kafkaconsumer.model.BankAccountEntity;
import com.shotin.kafkaconsumer.model.BankAccountInfo;
import com.shotin.kafkaconsumer.repository.BankAccountRepository;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.UUID;

import static com.shotin.kafkaconsumer.listener.BankAccountAndAddressesProcessor2Steps.*;

@Profile("2-steps")
@EnableBinding(BankAccountAndAddressesProcessor2Steps.class)
public class BankAccountInfoStream2Steps {

    private final Logger LOG = LoggerFactory.getLogger(BankAccountInfoStream2Steps.class);

    private BankAccountRepository bankAccountRepository;
    private BankAccountInfoConverter bankAccountInfoConverter;

    public BankAccountInfoStream2Steps(@Autowired BankAccountRepository bankAccountRepository,
                                      @Autowired BankAccountInfoConverter bankAccountInfoConverter) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountInfoConverter = bankAccountInfoConverter;
    }

    @StreamListener
    @SendTo(BANK_ACCOUNT_INFOS_OUTPUT)
    public KStream<UUID, JoinedBankAccountInfo> produceBankAccountInfo(
            @Input(BANK_ACCOUNTS_INPUT) KTable<UUID, BankAccount> bankAccountsTable,
            @Input(ADDRESSES_INPUT) KTable<UUID, Address> addressesTable) {

        return bankAccountsTable.leftJoin(addressesTable, (bankAccount, address) ->
                    new JoinedBankAccountInfo(bankAccount.getUuid(), bankAccount, address)
                )
                .toStream();
    }

    @StreamListener
    public void saveToCassandra(
            @Input(BANK_ACCOUNT_INFOS_INPUT) KTable<UUID, JoinedBankAccountInfo> joinedBankAccountInfoTable) {

        joinedBankAccountInfoTable.toStream()
                .foreach((key, joinedBankAccountInfo) -> {
                    LOG.info("Processing joinedBankAccount info in KTable.foreach key="+key+
                            " HAS address="+String.valueOf(joinedBankAccountInfo.getAddress() != null).toUpperCase());
                    BankAccountInfo bankAccountInfo = bankAccountInfoConverter.from(joinedBankAccountInfo);
                    bankAccountRepository.save(bankAccountInfo);
                });
    }
}
