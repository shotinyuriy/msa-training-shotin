package com.shotin.addressgenerator.transformer;

import com.shotin.addressgenerator.random.RandomProvider;
import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.AddressParts;
import com.shotin.bankaccount.model.kafka.BankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LocalBankAccountToAddressTransformer implements BankAccountToAddressTransformer {

    private final Logger LOG = LoggerFactory.getLogger(LocalBankAccountToAddressTransformer.class);

    private RandomProvider randomProvider;

    public LocalBankAccountToAddressTransformer(@Autowired RandomProvider randomProvider) {
        this.randomProvider = randomProvider;
    }

    @Override
    public Address transform(UUID key, BankAccount bankAccount) {
        LOG.info("Transforming key="+key);
        Address address = new Address();
        int streetIdx = randomProvider.nextInt(AddressParts.STREETS.length);
        int cityIdx = randomProvider.nextInt(AddressParts.CITIES.length);
        int stateIdx = randomProvider.nextInt(AddressParts.STATES.length);

        address.setUuid(key);
        address.setStreet(AddressParts.STREETS[streetIdx]);
        address.setCity(AddressParts.CITIES[cityIdx]);
        address.setState(AddressParts.STATES[stateIdx]);

        LOG.info("Generated address="+address);

        return address;
    }
}
