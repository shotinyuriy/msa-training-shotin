package com.shotin.userredisrequest.rest;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import com.shotin.userredisrequest.rest.model.BankAccountInfoKeys;
import com.shotin.userredisrequest.service.BankAccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/bank-account-infos")
public class BankAccountInfoRestResource {

    private BankAccountInfoService bankAccountInfoService;

    public BankAccountInfoRestResource(@Autowired BankAccountInfoService bankAccountInfoService) {
        this.bankAccountInfoService = bankAccountInfoService;
    }

    @GetMapping("/keys")
    public Mono<BankAccountInfoKeys> bankAccountInfoKeys() {
        return bankAccountInfoService.findAllKeys()
                .collect(ArrayList<String>::new, ArrayList::add)
                .map(BankAccountInfoKeys::new);
    }

    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BankAccountInfoEntity>> getBankAccountInfoById(@PathVariable String uuid) {
        return bankAccountInfoService
                .findById(uuid)
                .flatMap(bankAccountInfoEntity -> Mono.just(ResponseEntity.ok(bankAccountInfoEntity)))
                .onErrorReturn(ResponseEntity.<BankAccountInfoEntity>notFound().build());
    }
}
