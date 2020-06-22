package com.shotin.userredisrequest.rest;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import com.shotin.userredisrequest.rest.model.BankAccountInfoKeys;
import com.shotin.userredisrequest.service.BankAccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1-sync/bank-account-infos")
public class SyncBankAccountInfoRestResource {

    private final BankAccountInfoService bankAccountInfoService;

    public SyncBankAccountInfoRestResource(@Autowired BankAccountInfoService bankAccountInfoService) {
        this.bankAccountInfoService = bankAccountInfoService;
    }

    @GetMapping("/keys")
    public ResponseEntity<BankAccountInfoKeys> bankAccountInfoKeys() throws ExecutionException, InterruptedException {
        return bankAccountInfoService.findAllKeys()
                .collect(ArrayList<String>::new, ArrayList::add)
                .map(BankAccountInfoKeys::new)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .toFuture()
                .get();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BankAccountInfoEntity> getBankAccountInfoById(@PathVariable String uuid) throws ExecutionException, InterruptedException {
        return bankAccountInfoService.findById(uuid)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
                .toFuture()
                .get();
    }
}
