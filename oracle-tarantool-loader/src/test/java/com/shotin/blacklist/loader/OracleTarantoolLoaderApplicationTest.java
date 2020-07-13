/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.shotin.blacklist.loader;

import com.shotin.blacklist.loader.oracle.model.BankAccountInfoEntity;
import com.shotin.blacklist.loader.oracle.repository.BankAccountInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
@Slf4j
class OracleTarantoolLoaderApplicationTest {

    Logger LOG = LoggerFactory.getLogger(OracleTarantoolLoaderApplicationTest.class);

    @Autowired
    BankAccountInfoRepository bankAccountInfoRepository;

    @Test
    public void bankAccountInfoRepositoryTest() {
        long bankAccountInfoCount = bankAccountInfoRepository.count();
        Page<BankAccountInfoEntity> bankAccountInfoPage = bankAccountInfoRepository.findAll(PageRequest.of(1, 10));

        Assertions.assertNotNull(bankAccountInfoPage);

        LOG.error(() -> "BANK ACCOUNT INFO COUNT = "+bankAccountInfoCount);
        log.info("BANK ACCOUNT INFO COUNT = "+bankAccountInfoCount);
    }
}
