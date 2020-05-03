package com.shotin.kafkaconsumer.repository;

import com.datastax.driver.core.Cluster;
import com.shotin.kafkaconsumer.config.CassandraConfig;
import com.shotin.kafkaconsumer.config.TestCassandraConfig;
import com.shotin.kafkaconsumer.model.AccountType;
import com.shotin.kafkaconsumer.model.BankAccountEntity;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static com.shotin.kafkaconsumer.model.BankAccountEntity.BANK_ACCOUNT_TABLE;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath*:application.yml")
@ContextConfiguration(classes = {TestCassandraConfig.class})
public class BankAccountRepositoryTest {

    @Autowired
    protected BankAccountRepository bankAccountRepository;

    @Autowired
    protected CassandraAdminTemplate cassandraAdminTemplate;

    @BeforeClass
    public static void startCassandra() throws InterruptedException, IOException, TTransportException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        Cluster cluster = Cluster.builder()
                .addContactPoint("127.0.0.1").withPort(9142).build();
        cluster.connect();
    }

    @AfterClass
    public static void stopCassandra() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }

    @Before
    public void createTable() {
        cassandraAdminTemplate.createTable(true, CqlIdentifier.of(BANK_ACCOUNT_TABLE),
                BankAccountEntity.class, new HashMap<>());
    }

    @After
    public void dropTable() {
        cassandraAdminTemplate.dropTable(BankAccountEntity.class);
    }

    @Test
    public void testCRUD() {
        UUID bankAccountId = UUID.randomUUID();
        BankAccountEntity bankAccount = new BankAccountEntity();
        bankAccount.setUuid(bankAccountId);
        bankAccount.setFirstName("Иван");
        bankAccount.setLastName("Иванов");
        bankAccount.setLastName("Иваныч");
        bankAccount.setAccountNumber(33L);
        bankAccount.setAccountType(AccountType.CHECKING);

        bankAccountRepository.save(bankAccount);

        Optional<BankAccountEntity> foundBankAccount = bankAccountRepository.findById(bankAccountId);
        Assert.assertTrue(foundBankAccount.isPresent());

        bankAccountRepository.delete(bankAccount);

        foundBankAccount = bankAccountRepository.findById(bankAccountId);
        Assert.assertFalse(foundBankAccount.isPresent());
    }


}
