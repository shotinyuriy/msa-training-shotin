package com.shotin.consumer.redis.repository;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BankAccountInfoRepository extends CrudRepository<BankAccountInfoEntity, UUID> {

}
