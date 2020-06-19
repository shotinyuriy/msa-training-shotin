package com.shotin.consumer.redis.converter;

import com.shotin.bankaccount.model.kafka.Address;
import com.shotin.bankaccount.model.kafka.BankAccount;
import com.shotin.bankaccount.model.kafka.JoinedBankAccountInfo;
import com.shotin.consumer.redis.model.AccountType;
import com.shotin.consumer.redis.model.BankAccountInfoEntity;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class BankAccountInfoConverterTest {

    private static final UUID UUID_1 = UUID.randomUUID();
    private static final String FIRST_NAME = "Firstname";
    private static final String LAST_NAME = "Lastname";
    private static final String PATRONYMIC = "Patronymic";
    private static final long ACCOUNT_NUMBER = 123456789L;
    private static final AccountType ACCOUNT_TYPE = AccountType.CHECKING;
    private static final String STREET = "Street";
    private static final String CITY = "City";
    private static final String STATE = "State";

    BankAccountConverter bankAccountConverter = new BankAccountConverter();
    AddressConverter addressConverter = new AddressConverter();
    BankAccountInfoConverter bankAccountInfoConverter
            = new BankAccountInfoConverter(addressConverter, bankAccountConverter);

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFrom_AddressAndBankAccountAreNull() {
        JoinedBankAccountInfo joinedBankAccountInfo = new JoinedBankAccountInfo();

        BankAccountInfoEntity bankAccountInfo = bankAccountInfoConverter.from(joinedBankAccountInfo);

        assertNull(bankAccountInfo);
    }

    @Test
    public void testFrom_AddressIsNull() {
        UUID uuid = UUID.randomUUID();
        BankAccount bankAccount = new BankAccount();
        JoinedBankAccountInfo joinedBankAccountInfo = new JoinedBankAccountInfo(uuid, bankAccount, null);

        BankAccountInfoEntity bankAccountInfo = bankAccountInfoConverter.from(joinedBankAccountInfo);

        assertNotNull(bankAccountInfo);
        assertNotNull(bankAccountInfo.getBankAccount());
        assertNull(bankAccountInfo.getAddress());
    }

    @Test
    public void testFrom_AllFields() {
        UUID uuid = UUID.randomUUID();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setFirstName(FIRST_NAME);
        bankAccount.setLastName(LAST_NAME);
        bankAccount.setPatronymic(PATRONYMIC);
        bankAccount.setAccountNumber(ACCOUNT_NUMBER);
        bankAccount.setAccountType(ACCOUNT_TYPE);
        bankAccount.setUuid(UUID_1);

        Address address = new Address();
        address.setStreet(STREET);
        address.setCity(CITY);
        address.setState(STATE);

        JoinedBankAccountInfo joinedBankAccountInfo = new JoinedBankAccountInfo(uuid, bankAccount, address);

        BankAccountInfoEntity bankAccountInfo = bankAccountInfoConverter.from(joinedBankAccountInfo);

        assertNotNull(bankAccountInfo);
        assertNotNull(bankAccountInfo.getBankAccount());
        assertNotNull(bankAccountInfo.getAddress());

        assertEquals(UUID_1, bankAccountInfo.getBankAccount().getUuid());
        assertEquals(FIRST_NAME, bankAccountInfo.getBankAccount().getFirstName());
        assertEquals(LAST_NAME, bankAccountInfo.getBankAccount().getLastName());
        assertEquals(PATRONYMIC, bankAccountInfo.getBankAccount().getPatronymic());
        assertEquals(ACCOUNT_NUMBER, bankAccountInfo.getBankAccount().getAccountNumber());
        assertEquals(ACCOUNT_TYPE, bankAccountInfo.getBankAccount().getAccountType());

        assertEquals(STREET, bankAccountInfo.getAddress().getStreet());
        assertEquals(CITY, bankAccountInfo.getAddress().getCity());
        assertEquals(STATE, bankAccountInfo.getAddress().getState());
    }


}
