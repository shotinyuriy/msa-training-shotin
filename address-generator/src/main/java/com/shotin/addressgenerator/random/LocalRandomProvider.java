package com.shotin.addressgenerator.random;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Profile("local-random")
@Component
public class LocalRandomProvider implements RandomProvider {
    private Random random = new Random(System.currentTimeMillis());

    public String randomizeString(String input, int length) {
        if(input == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int inputLength = input.length();
        for(int i = 0; i < length; i++) {
            sb.append(input.charAt(random.nextInt(inputLength)));
        }
        return sb.toString();
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
