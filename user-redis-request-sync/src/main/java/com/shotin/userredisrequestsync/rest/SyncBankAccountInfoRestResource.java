package com.shotin.userredisrequestsync.rest;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import com.shotin.userredisrequestsync.rest.model.BankAccountInfoKeys;
import com.shotin.userredisrequestsync.service.BankAccountInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/v1-sync/bank-account-infos")
@RequiredArgsConstructor
public class SyncBankAccountInfoRestResource {

    private final BankAccountInfoService bankAccountInfoService;

    @GetMapping("/keys")
    public ResponseEntity<BankAccountInfoKeys> bankAccountInfoKeys() {
        final AtomicReference<Throwable> exception = new AtomicReference<>();
        BankAccountInfoKeys keys = bankAccountInfoService.findAllKeys()
                .collect(ArrayList<String>::new, ArrayList::add)
                .map(BankAccountInfoKeys::new)
                .doOnError(ex -> exception.set(ex))
                .block();

        if (exception.get() != null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            return ResponseEntity.ok(keys);
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BankAccountInfoEntity> getBankAccountInfoById(@PathVariable String uuid) {
        final AtomicReference<Throwable> exception = new AtomicReference<>();
        BankAccountInfoEntity bankAccountInfo = bankAccountInfoService.findBankAccountInfoById(uuid)
                .doOnError(ex -> exception.set(ex))
                .block();
        if (exception.get() != null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            return ResponseEntity.ok(bankAccountInfo);
        }
    }
}
