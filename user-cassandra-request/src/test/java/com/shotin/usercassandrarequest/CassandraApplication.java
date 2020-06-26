package com.shotin.usercassandrarequest;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.EndPoint;
import com.shotin.usercassandrarequest.model.AccountType;
import com.shotin.usercassandrarequest.model.AddressEntity;
import com.shotin.usercassandrarequest.model.BankAccountEntity;
import com.shotin.usercassandrarequest.model.BankAccountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Criteria;
import org.springframework.data.cassandra.core.query.Query;

import java.util.UUID;

public class CassandraApplication {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraApplication.class);

    protected static BankAccountInfo newBankAccountInfo(UUID uuid, String fullname, long accountNumber, AccountType accountType, String address) {

        String[] names = fullname.split(" ");
        String[] addressParts = address.split(",");

        BankAccountEntity bankAccount = new BankAccountEntity();
        if (names.length > 0) {
            bankAccount.setLastName(names[0]);
        }
        if (names.length > 1) {
            bankAccount.setFirstName(names[1]);
        }
        if (names.length > 2) {
            bankAccount.setPatronymic(names[2]);
        }
        bankAccount.setAccountNumber(accountNumber);
        bankAccount.setAccountType(accountType);

        AddressEntity addressEntity = new AddressEntity();
        if (addressParts.length > 0) {
            addressEntity.setState(addressParts[0]);
        }
        if (addressParts.length > 1) {
            addressEntity.setCity(addressParts[1]);
        }
        if (addressParts.length > 2) {
            addressEntity.setStreet(addressParts[2]);
        }

        BankAccountInfo bankAccountInfo = new BankAccountInfo(uuid, bankAccount, addressEntity);
        return bankAccountInfo;
    }

    public static void main(String[] args) {

        CqlSession cqlSession = CqlSession.builder()
                .withKeyspace("test_keyspace")
                .withLocalDatacenter("datacenter1")
                .build();

        CassandraOperations template = new CassandraTemplate(cqlSession);

        BankAccountInfo bankAccountInfo = newBankAccountInfo(
                UUID.randomUUID(),
                "Тестовый Тест Тестович",
                1234567,
                AccountType.CHECKING,
                "Тестовая область, Тестград, Тестовая ул. 1"
        );



        BankAccountInfo savedBankAccountInfo = template.insert(bankAccountInfo);

        LOG.info("SAVED BANK ACCOUNT INFO = "+savedBankAccountInfo);

        BankAccountInfo bankAccountInfoFound = template.selectOne(
                Query.query(Criteria.where("uuid").is(bankAccountInfo.getUuid())),
                BankAccountInfo.class);

        LOG.info("FOUND BANK ACCOUNT INFO = "+savedBankAccountInfo);

        template.truncate(BankAccountInfo.class);
        cqlSession.close();
    }
}
