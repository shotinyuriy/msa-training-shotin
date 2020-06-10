package com.shotin.bankaccountgenerator.generator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FromFileNamesGeneratorTest {

    private FromFileNamesGenerator fromFileNamesGenerator;

    @Before
    public void setUp() {
        fromFileNamesGenerator = new FromFileNamesGenerator();
        fromFileNamesGenerator.setFirstNamesFilePath("classpath*:TestNames.txt");
        fromFileNamesGenerator.setLastNamesFilePath("classpath*:TestNames.txt");
        fromFileNamesGenerator.setPatronymicsFilePath("classpath*:TestNames.txt");
        fromFileNamesGenerator.readAllNames();
    }

    @Test
    public void testGetRandomFirstName() {
        String firstName = fromFileNamesGenerator.getRandomFirstName();
        Assert.assertNotNull(firstName);
        Assert.assertEquals("Один", firstName);
    }

    @Test
    public void testGetRandomLastName() {
        String lastName = fromFileNamesGenerator.getRandomLastName();
        Assert.assertNotNull(lastName);
        Assert.assertEquals("Один", lastName);
    }

    @Test
    public void testGetRandomPatronimic() {
        String patronymic = fromFileNamesGenerator.getRandomPatronymic();
        Assert.assertNotNull(patronymic);
        Assert.assertEquals("Один", patronymic);
    }


}
