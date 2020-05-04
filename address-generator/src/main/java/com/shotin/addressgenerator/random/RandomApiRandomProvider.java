package com.shotin.addressgenerator.random;

import com.shotin.addressgenerator.rest.model.RandomApiResponse;
import com.shotin.addressgenerator.rest.model.RandomNumericResult;
import com.shotin.addressgenerator.service.RandomApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Profile("remote-random-api")
@Component
public class RandomApiRandomProvider implements RandomProvider {

    @Autowired
    private RandomApiClient randomApiClient;

    @Override
    public int nextInt(int bound) {
        RandomApiResponse response = randomApiClient.getRandomNumeric(bound - 1) // this api includes the bound but for java we exclude it.
                .block(Duration.ofMillis(5000L));
        if (response != null && response.getResults() != null && !response.getResults().isEmpty()) {
            if (response.getResults().get(0).getValue() != null) {
                return response.getResults().get(0).getValue();
            }
        }
        throw new NullPointerException("RandomNumericResult was null, in bound = " + bound);
    }
}
