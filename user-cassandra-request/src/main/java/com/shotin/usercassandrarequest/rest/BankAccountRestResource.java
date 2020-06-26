package com.shotin.usercassandrarequest.rest;

import com.shotin.usercassandrarequest.model.AddressEntity;
import com.shotin.usercassandrarequest.model.BankAccountInfo;
import com.shotin.usercassandrarequest.repository.BankAccountRepository;
import com.shotin.usercassandrarequest.rest.model.BankAccountInfoKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountRestResource {

    private BankAccountRepository bankAccountRepository;

    public BankAccountRestResource(@Autowired BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @GetMapping("/keys")
    public ResponseEntity<Object> getBankAccountInfoKeys() {
        Set<String> keys = bankAccountRepository
                .findAll()
                .stream()
                .map(bankAccountInfo -> bankAccountInfo.getUuid().toString())
                .collect(Collectors.toSet());


        return ResponseEntity.ok(new BankAccountInfoKeys(keys));
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getBankAccountByUuid(@RequestParam String street,
                                                       @RequestParam String city,
                                                       @RequestParam String state) {
        try {
            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setStreet(street);
            addressEntity.setCity(city);
            addressEntity.setState(state);

            List<BankAccountInfo> bankAccountInfos = bankAccountRepository.findByAddress(addressEntity);
            if(bankAccountInfos.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(bankAccountInfos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Object> getBankAccountByUuid(@PathVariable UUID uuid) {
        Optional<BankAccountInfo> bankAccount = bankAccountRepository.findById(uuid);
        if(!bankAccount.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bankAccount.get());
    }

}
