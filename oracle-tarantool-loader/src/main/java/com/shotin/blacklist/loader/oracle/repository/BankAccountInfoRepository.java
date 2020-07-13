package com.shotin.blacklist.loader.oracle.repository;

import com.shotin.blacklist.loader.oracle.model.BankAccountInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountInfoRepository extends JpaRepository<BankAccountInfoEntity, UUID> {
}
