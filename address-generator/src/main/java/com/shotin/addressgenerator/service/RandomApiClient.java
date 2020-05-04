package com.shotin.addressgenerator.service;

import com.shotin.addressgenerator.rest.model.RandomApiResponse;
import com.shotin.addressgenerator.rest.model.RandomNumericResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class RandomApiClient {

    @Value("${random-api.base-url}")
    private String baseUrl;

    @Value("${random-api.random-numeric-uri}")
    private String randomNumericUri;

    private WebClient webClient;

    @PostConstruct
    protected void initialize() {
        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<RandomApiResponse> getRandomNumeric(int max) {
        Mono<RandomApiResponse> randomNumericResponseMono = webClient.get()
                .uri(randomNumericUri, max)
                .retrieve()
                .bodyToMono(RandomApiResponse.class);
        return randomNumericResponseMono;
    }
}
