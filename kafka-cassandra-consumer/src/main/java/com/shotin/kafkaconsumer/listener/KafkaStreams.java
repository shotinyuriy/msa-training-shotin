package com.shotin.kafkaconsumer.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.kafkaconsumer.config.KafkaTopics;
import com.shotin.kafkaconsumer.converter.BankAccountInfoConverter;
import com.shotin.kafkaconsumer.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Profile("kafka-streams")
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaStreams {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountInfoConverter bankAccountInfoConverter;

    @Bean
    public KStream<UUID, JoinedBankAccountInfo> bankAccountInfoJoiner(
            KafkaTopics kafkaTopics, StreamsBuilder streamsBuilder) {

        KTable<UUID, BankAccount> bankAccounts = streamsBuilder
                .table(kafkaTopics.getBankAccountsTopic(),
                Consumed.with(Serdes.UUID(), new JsonSerde<>(BankAccount.class, new ObjectMapper())));

        KTable<UUID, Address> addresses = streamsBuilder
                .table(kafkaTopics.getAddressesTopic(),
                        Consumed.with(Serdes.UUID(), new JsonSerde<>(Address.class, new ObjectMapper())));

        return bankAccounts.join(addresses, (bankAccount, address) ->
                new JoinedBankAccountInfo(bankAccount.getUuid(), bankAccount, address))
//                .groupBy(new KeyValueMapper<UUID, JoinedBankAccountInfo, KeyValue<UUID, JoinedBankAccountInfo>>() {
//                    @Override
//                    public KeyValue<UUID, JoinedBankAccountInfo> apply(UUID key, JoinedBankAccountInfo value) {
//                        return new KeyValue<UUID, JoinedBankAccountInfo>(key, value);
//                    }
//                })
                .toStream()
                .peek((key, value) -> log.info("RECEIVED MESSAGE UUID={}", key))
                .peek(((key, value) -> {
                    try {
                        bankAccountRepository.save(bankAccountInfoConverter.from(value));
                        log.info("SAVED MESSAGE TO CASSANDRA. UUID={}", key);
                    } catch (Exception e) {
                        log.error("FAILED SAVING MESSAGE TO CASSANDRA. UUID={}", key);
                    }
                }))
                ;
    }
}
