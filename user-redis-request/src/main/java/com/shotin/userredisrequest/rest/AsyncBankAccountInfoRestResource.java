package com.shotin.userredisrequest.rest;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import com.shotin.userredisrequest.rest.model.BankAccountInfoKeys;
import com.shotin.userredisrequest.service.BankAccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1-async/bank-account-infos")
public class AsyncBankAccountInfoRestResource {

    private final BankAccountInfoService bankAccountInfoService;

    public AsyncBankAccountInfoRestResource(@Autowired BankAccountInfoService bankAccountInfoService) {
        this.bankAccountInfoService = bankAccountInfoService;
    }

    @Async("asyncExecutor")
    @GetMapping("/keys")
    public CompletableFuture<ResponseEntity<BankAccountInfoKeys>> getAllKeys() {
        return bankAccountInfoService
                .findAllKeys()
                .collect(ArrayList<String>::new, ArrayList::add)
                .map(BankAccountInfoKeys::new)
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .toFuture();
    }

    @Async("asyncExecutor")
    @GetMapping("/{uuid}")
    public CompletableFuture<ResponseEntity<BankAccountInfoEntity>> getBankAccountInfoById(@PathVariable String uuid) {
        return bankAccountInfoService
                .findById(uuid)
                .map(bankAccountInfo -> ResponseEntity.ok(bankAccountInfo))
                .onErrorResume(ex -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .toFuture();
    }
}
