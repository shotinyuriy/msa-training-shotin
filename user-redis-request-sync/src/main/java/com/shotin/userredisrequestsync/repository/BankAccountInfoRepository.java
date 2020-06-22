package com.shotin.userredisrequestsync.repository;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountInfoRepository extends CrudRepository<BankAccountInfoEntity, String> {
}
