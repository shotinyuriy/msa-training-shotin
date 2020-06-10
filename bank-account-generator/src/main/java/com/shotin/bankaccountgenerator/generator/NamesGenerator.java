package com.shotin.bankaccountgenerator.generator;

import com.shotin.bankaccountgenerator.model.Gender;

public interface NamesGenerator {

    String getRandomFirstName();
    String getRandomLastName();
    String getRandomPatronymic();
}
