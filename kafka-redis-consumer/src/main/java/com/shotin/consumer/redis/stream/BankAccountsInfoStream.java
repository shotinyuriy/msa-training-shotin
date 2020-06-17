package com.shotin.consumer.redis.stream;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.consumer.redis.converter.BankAccountInfoConverter;
import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import com.shotin.consumer.redis.repository.BankAccountInfoRepository;
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

import static com.shotin.consumer.redis.stream.BankAccountAndAddressProcessor.*;

@Profile("!test")
@EnableBinding(BankAccountAndAddressProcessor.class)
public class BankAccountsInfoStream {

    private Logger LOG = LoggerFactory.getLogger(LoggerFactory.class);

    private BankAccountInfoConverter bankAccountInfoConverter;
    private BankAccountInfoRepository bankAccountInfoRepository;

    public BankAccountsInfoStream(@Autowired BankAccountInfoConverter bankAccountInfoConverter,
                                  @Autowired BankAccountInfoRepository bankAccountInfoRepository) {
        this.bankAccountInfoConverter = bankAccountInfoConverter;
        this.bankAccountInfoRepository = bankAccountInfoRepository;
    }

    @StreamListener
    @SendTo(BANK_ACCOUNT_INFOS_OUTPUT)
    public KStream<UUID, JoinedBankAccountInfo> joinBankAccountInfo(
            @Input(BANK_ACCOUNTS_INPUT) KTable<UUID, BankAccount> bankAccountTable,
            @Input(ADDRESSES_INPUT) KTable<UUID, Address> addressTable) {
        return bankAccountTable.join(addressTable, (bankAccount, address) ->
                new JoinedBankAccountInfo(bankAccount.getUuid(), bankAccount, address)
        ).toStream();
    }

    @StreamListener
    public void saveToRedis(
            @Input(BANK_ACCOUNT_INFOS_INPUT) KStream<UUID, JoinedBankAccountInfo> joinedBankAccountInfos) {
        joinedBankAccountInfos
//                .peek((uuid, joinBankAccountInfo) -> joinBankAccountInfo.setExecutionTime(System.currentTimeMillis()))
                .peek((uuid, joinBankAccountInfo) -> LOG.info("START {} = {}", uuid, System.currentTimeMillis()))
                .peek((uuid, joinedBankAccountInfo) -> {
                    BankAccountInfoEntity bankAccountInfo = bankAccountInfoConverter.from(joinedBankAccountInfo);
                    bankAccountInfoRepository.save(bankAccountInfo);
                })
                .peek((uuid, joinBankAccountInfo) -> LOG.info("END {} = {}", uuid, System.currentTimeMillis()));
//                .foreach((uuid, joinedBankAccountInfo) -> {
//                    long executionTime = (System.currentTimeMillis() - joinedBankAccountInfo.getExecutionTime());
//                    LOG.info("EXECUTION TIME FOR {} = {}", uuid, executionTime);
//                });
    }
}
