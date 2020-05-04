package com.shotin.kafkaconsumer.repository;

import com.shotin.kafkaconsumer.model.BankAccountEntity;
import com.shotin.kafkaconsumer.model.BankAccountInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends CassandraRepository<BankAccountInfo, UUID> {
}
