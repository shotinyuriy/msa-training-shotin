package com.shotin.userredisrequestsync.service;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BankAccountInfoService {

    private final com.shotin.userredisrequestsync.repository.BankAccountInfoRepository bankAccountInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public Set<String> findAllKeys() {
        return redisTemplate.opsForSet().members(BankAccountInfoEntity.BANK_ACCOUNT_INFO_KEY);
    }

    public Optional<BankAccountInfoEntity> findBankAccountInfoById(String uuid) {
        return bankAccountInfoRepository.findById(uuid);
    }
}