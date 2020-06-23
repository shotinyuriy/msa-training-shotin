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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1-sync/bank-account-infos")
@RequiredArgsConstructor
public class SyncBankAccountInfoRestResource {

    private final BankAccountInfoService bankAccountInfoService;

    @GetMapping("/keys")
    public ResponseEntity<BankAccountInfoKeys> bankAccountInfoKeys() {
        try {
            BankAccountInfoKeys keys = new BankAccountInfoKeys(
                    new ArrayList<>(bankAccountInfoService.findAllKeys()));
            return ResponseEntity.ok(keys);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BankAccountInfoEntity> getBankAccountInfoById(@PathVariable String uuid) {
        try {
            Optional<BankAccountInfoEntity> bankAccountInfo = bankAccountInfoService.findBankAccountInfoById(uuid);
            return bankAccountInfo
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
