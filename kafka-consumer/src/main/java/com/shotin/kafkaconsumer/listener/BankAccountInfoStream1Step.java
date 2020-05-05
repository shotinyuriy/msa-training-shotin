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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.SendTo;

import java.util.UUID;

import static com.shotin.kafkaconsumer.listener.BankAccountAndAddressesProcessor.*;

@Profile("1-step")
@EnableBinding(BankAccountAndAddressesProcessor.class)
public class BankAccountInfoStream1Step {

    private final Logger LOG = LoggerFactory.getLogger(BankAccountInfoStream1Step.class);

    @Value("${bank-account-info-stream.1-step.enabled:false}")
    private boolean enabled;

    private BankAccountRepository bankAccountRepository;
    private BankAccountInfoConverter bankAccountInfoConverter;

    public BankAccountInfoStream1Step(@Autowired BankAccountRepository bankAccountRepository,
                                      @Autowired BankAccountInfoConverter bankAccountInfoConverter) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountInfoConverter = bankAccountInfoConverter;
    }

    @StreamListener
    public void produceBankAccountInfo(
            @Input(BANK_ACCOUNTS_INPUT) KTable<UUID, BankAccount> bankAccountsTable,
            @Input(ADDRESSES_INPUT) KTable<UUID, Address> addressesTable) {

        bankAccountsTable.leftJoin(addressesTable, (bankAccount, address) ->
                    new JoinedBankAccountInfo(bankAccount.getUuid(), bankAccount, address)
                )
                .toStream()
                .foreach((key, joinedBankAccountInfo) -> {
                    LOG.info("Processing joinedBankAccount info in KTable.foreach key="+key+
                            " HAS address="+String.valueOf(joinedBankAccountInfo.getAddress() != null).toUpperCase());
                    BankAccountInfo bankAccountInfo = bankAccountInfoConverter.from(joinedBankAccountInfo);
                    bankAccountRepository.save(bankAccountInfo);
                });
    }
}
