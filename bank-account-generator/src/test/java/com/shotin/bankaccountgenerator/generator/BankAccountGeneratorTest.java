package com.shotin.bankaccountgenerator.generator;

import com.shotin.bankaccountgenerator.model.BankAccount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BankAccountGeneratorTest {

    private BankAccountGenerator bankAccountGenerator;

    @Before
    public void setUp() {
        NamesGenerator manNamesGenerator = Mockito.mock(NamesGenerator.class);
        Mockito.when(manNamesGenerator.getRandomFirstName()).thenReturn("FirstName");
        Mockito.when(manNamesGenerator.getRandomLastName()).thenReturn("LastName");
        Mockito.when(manNamesGenerator.getRandomPatronymic()).thenReturn("Patronimic");

        NamesGenerator womanNamesGenerator = Mockito.mock(NamesGenerator.class);
        Mockito.when(womanNamesGenerator.getRandomFirstName()).thenReturn("FirstName");
        Mockito.when(womanNamesGenerator.getRandomLastName()).thenReturn("LastName");
        Mockito.when(womanNamesGenerator.getRandomPatronymic()).thenReturn("Patronimic");

        bankAccountGenerator = new BankAccountGenerator(manNamesGenerator, womanNamesGenerator);
    }

    @Test
    public void testGenerateBankAccount() {
        BankAccount bankAccount = bankAccountGenerator.generateBankAccount();
        Assert.assertNotNull(bankAccount);
        Assert.assertNotNull(bankAccount.getUuid());
        Assert.assertEquals("FirstName", bankAccount.getFirstName());
        Assert.assertEquals("LastName", bankAccount.getLastName());
        Assert.assertEquals("Patronimic", bankAccount.getPatronymic());
        Assert.assertTrue(bankAccount.getAccountNumber() > 0);
    }
}
