package com.shotin.kafkaproducer.service;

import com.shotin.kafkaproducer.model.BankAccount;
import com.shotin.kafkaproducer.model.BankAccountList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class BankAccountGenerator {

    @Value("${bank-account-generator.timeout-millis}")
    private long timeoutMillis;

    @Value("${bank-account-generator.baseUrl}")
    private String baseUrl;

    @Value("${bank-account-generator.get-random-uri}")
    private String getRandomUri;

    @Value("${bank-account-generator.get-many-random-uri}")
    private String getRandomCountUri;

    private WebClient webClient;

    @PostConstruct
    protected void initialize() {
        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Flux<BankAccount> getRandomAccountNonBlocking() {
        Flux<BankAccount> bankAccountFlux = webClient
                .get()
                .uri(getRandomUri)
                .retrieve()
                .bodyToFlux(BankAccount.class);

        return bankAccountFlux;
    }

    public Flux<BankAccountList> getManyRandomAccountsNonBlocking(int count) {
        Flux<BankAccountList> bankAccountFlux = webClient
                .get()
                .uri(getRandomCountUri, count)
                .retrieve()
                .bodyToFlux(BankAccountList.class);

        return bankAccountFlux;
    }

    public BankAccountList getManyRandomAccounts(int count) {
        Mono<BankAccountList> bankAccountListMono = webClient
                .get()
                .uri(getRandomCountUri, count)
                .retrieve()
                .bodyToMono(BankAccountList.class);

        return bankAccountListMono.block(Duration.ofMillis(timeoutMillis));
    }
}

