package com.shotin.usercassandrarequest.rest.model;

import java.util.Set;

public class BankAccountInfoKeys {
    private Set<String> keys;

    public BankAccountInfoKeys(Set<String> keys) {
        this.keys = keys;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }
}
