package com.shotin.kafkaconsumer.listener;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
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
import org.springframework.messaging.handler.annotation.SendTo;

import static com.shotin.kafkaconsumer.listener.BankAccountAndAddressesProcessor.*;

@EnableBinding(BankAccountAndAddressesProcessor.class)
public class BankAccountInfoStream {

    private final Logger LOG = LoggerFactory.getLogger(BankAccountInfoStream.class);

    private BankAccountRepository bankAccountRepository;

    public BankAccountInfoStream(@Autowired BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @StreamListener
    @SendTo(BANK_ACCOUNT_INFOS_OUTPUT)
    public KStream<String, JoinedBankAccountInfo> produceBankAccountInfo(
            @Input(BANK_ACCOUNTS_INPUT) KTable<String, BankAccount> bankAccountsTable,
            @Input(ADDRESSES_INPUT) KTable<String, Address> addressesTable) {

        return bankAccountsTable.leftJoin(addressesTable, (bankAccount, address) ->
                    new JoinedBankAccountInfo(bankAccount.getUuid(), bankAccount, address)
                )
                .filter((key, joinedBankAccountInfo) -> joinedBankAccountInfo != null)
                .toStream();
    }

    @StreamListener
    public void saveToCassandra(
            @Input(BANK_ACCOUNT_INFOS_INPUT) KTable<String, JoinedBankAccountInfo> joinedBankAccountInfoTable) {

        joinedBankAccountInfoTable.toStream()
                .foreach((key, joinedBankAccountInfo) -> {
                    LOG.info("Processing joinedBankAccount info in KTable.foreach key="+key);
                    BankAccountEntity bankAccountEntity = new BankAccountEntity(joinedBankAccountInfo.getBankAccount());
                    AddressEntity addressEntity = null;
                    if (joinedBankAccountInfo.getAddress() != null) {
                        addressEntity = new AddressEntity(joinedBankAccountInfo.getAddress());
                    }
                    BankAccountInfo bankAccountInfo = new BankAccountInfo(bankAccountEntity.getUuid(), bankAccountEntity, addressEntity);
                    bankAccountRepository.save(bankAccountInfo);
                });
    }
}
