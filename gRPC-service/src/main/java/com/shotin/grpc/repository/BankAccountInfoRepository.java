package com.shotin.grpc.repository;

import com.shotin.grpc.model.BankAccountInfoEntity;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountInfoRepository extends ReactiveCassandraRepository<BankAccountInfoEntity, UUID> {

}
