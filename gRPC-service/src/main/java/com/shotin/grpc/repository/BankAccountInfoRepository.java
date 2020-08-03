package com.shotin.grpc.repository;

import com.shotin.grpc.model.BankAccountInfoEntity;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import java.util.UUID;

public interface BankAccountInfoRepository extends ReactiveCassandraRepository<BankAccountInfoEntity, UUID> {
}
