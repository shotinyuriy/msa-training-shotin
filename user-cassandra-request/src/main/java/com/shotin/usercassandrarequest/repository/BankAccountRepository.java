package com.shotin.usercassandrarequest.repository;

import com.shotin.usercassandrarequest.model.BankAccountEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends CassandraRepository<BankAccountEntity, UUID> {
}
