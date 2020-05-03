package com.shotin.kafkaconsumer.repository;

import com.shotin.kafkaconsumer.model.BankAccountEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends CassandraRepository<BankAccountEntity, UUID> {
}
