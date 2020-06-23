package com.shotin.usercassandrarequest.repository;

import com.shotin.usercassandrarequest.model.AddressEntity;
import com.shotin.usercassandrarequest.model.BankAccountInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankAccountRepository extends CassandraRepository<BankAccountInfo, UUID> {

//    @Query("SELECT * FROM bank_account_info WHERE address=?0")
    List<BankAccountInfo> findByAddress(AddressEntity address);
}
