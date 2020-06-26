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
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Profile("kafka-streams")
@Service
@Slf4j
@RequiredArgsConstructor
public class BankAccountInfoKafkaStreams {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountInfoConverter bankAccountInfoConverter;

    @Bean
    public Topology bankAccountInfoJoiner1(KafkaTopics kafkaTopics, StreamsBuilder streamsBuilder) {

        try (JsonSerde<BankAccount> bankAccountSerde = new JsonSerde<>(BankAccount.class, new ObjectMapper());
             JsonSerde<Address> addressSerde = new JsonSerde<>(Address.class, new ObjectMapper())) {

            KTable<UUID, BankAccount> bankAccounts
                    = streamsBuilder.table(kafkaTopics.getBankAccountsTopic(), Consumed.with(Serdes.UUID(), bankAccountSerde));

            KTable<UUID, Address> addresses
                    = streamsBuilder.table(kafkaTopics.getAddressesTopic(), Consumed.with(Serdes.UUID(), addressSerde));

            final Set<UUID> processedUuids = new HashSet<>();

            addresses
                    .join(bankAccounts, (address, bankAccount) -> {
                        log.info("JOINING BANK_ACCOUNT={}, ADDRESS={}", bankAccount.getUuid(), address.getUuid());
                        return new JoinedBankAccountInfo(bankAccount.getUuid(), bankAccount, address);
                    })
                    .toStream()
                    .filter((key, value) -> {
                        boolean duplicated = processedUuids.contains(key);
                        if (duplicated) {
                            log.warn("DUPLICATED KEY UUID={} PROCESSED SET SIZE={}", key, processedUuids.size());
                            processedUuids.remove(key);
                        }
                        return !duplicated && value != null;
                    })
                    .peek((key, value) -> processedUuids.add(key))
                    .peek((key, value) -> log.info("RECEIVED MESSAGE UUID={}", key))
                    .peek(((key, value) -> {
                        try {
                            bankAccountRepository.save(bankAccountInfoConverter.from(value));
                            log.info("SAVED MESSAGE TO CASSANDRA. UUID={}", key);
                        } catch (Exception e) {
                            log.error("FAILED SAVING MESSAGE TO CASSANDRA. UUID="+key+", EXCEPTION=", e);
                        }
                    }));

            return streamsBuilder.build();
        }
    }
}
