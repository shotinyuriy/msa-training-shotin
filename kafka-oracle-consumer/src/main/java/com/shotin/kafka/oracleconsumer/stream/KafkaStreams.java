package com.shotin.kafka.oracleconsumer.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.kafka.oracleconsumer.config.KafkaTopics;
import com.shotin.kafka.oracleconsumer.converter.AddressEntityConverter;
import com.shotin.kafka.oracleconsumer.converter.BankAccountEntityConverter;
import com.shotin.kafka.oracleconsumer.repository.AddressRepository;
import com.shotin.kafka.oracleconsumer.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaStreams {

    private final AddressRepository addressRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AddressEntityConverter addressEntityConverter;
    private final BankAccountEntityConverter bankAccountEntityConverter;

    @Bean
    public Topology streamTopology(KafkaTopics kafkaTopics, StreamsBuilder streamsBuilder) {
        try (
                JsonSerde<BankAccount> bankAccountSerde = new JsonSerde<BankAccount>(BankAccount.class, new ObjectMapper());
                JsonSerde<Address> addressSerde = new JsonSerde<Address>(Address.class, new ObjectMapper())) {

            streamsBuilder.stream(kafkaTopics.getBankAccountsTopic(), Consumed.with(Serdes.UUID(), bankAccountSerde))
                    .filter((key, value) -> value != null)
                    .foreach((key, value) -> bankAccountRepository.save(bankAccountEntityConverter.from(value)));


            streamsBuilder.stream(kafkaTopics.getAddressesTopic(), Consumed.with(Serdes.UUID(), addressSerde))
                    .filter((key, value) -> value != null)
                    .foreach((key, value) -> addressRepository.save(addressEntityConverter.from(value)));

        }
        return streamsBuilder.build();
    }
}
