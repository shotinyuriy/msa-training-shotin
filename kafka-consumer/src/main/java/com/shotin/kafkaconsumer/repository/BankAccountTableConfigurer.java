package com.shotin.kafkaconsumer.repository;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BankAccountTableConfigurer {

    @PostConstruct
    public void createBankAccountTable() {
    }
}
