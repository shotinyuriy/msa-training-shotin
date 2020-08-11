package com.shotin.blacklist.loader.oracle.repository;

import com.shotin.blacklist.loader.oracle.model.BankAccountInfoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankAccountInfoRepository extends JpaRepository<BankAccountInfoEntity, UUID> {

    int countByUpdateId(Long updateId);
    List<BankAccountInfoEntity> findAllByUpdateId(Long updateId, Pageable pageable);
}
