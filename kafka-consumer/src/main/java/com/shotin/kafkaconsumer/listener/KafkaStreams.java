package com.shotin.kafkaconsumer.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.kafkaconsumer.config.KafkaTopics;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Profile("kafka-streams")
@Service
public class KafkaStreams {

    @Bean
    public KStream<UUID, JoinedBankAccountInfo> bankAccountInfoJoiner(
            KafkaTopics kafkaTopics, StreamsBuilder streamsBuilder) {

        KTable<UUID, BankAccount> bankAccounts = streamsBuilder
                .table(kafkaTopics.getBankAccountsTopic(),
                Consumed.with(Serdes.UUID(), new JsonSerde<>(BankAccount.class, new ObjectMapper())));

        KTable<UUID, Address> addresses = streamsBuilder
                .table(kafkaTopics.getAddressesTopic(),
                        Consumed.with(Serdes.UUID(), new JsonSerde<>(Address.class, new ObjectMapper())));

        return bankAccounts.join(addresses, (bankAccount, address) -> {
            return new JoinedBankAccountInfo(bankAccount.getUuid(), bankAccount, address);
        }).toStream();
    }
}
