package com.shotin.kafka.oracleconsumer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="account_type")
public class AccountTypeEntity {
    public static final String CHECKING = "checking";
    public static final String SAVINGS = "savings";

    public AccountTypeEntity() {
    }

    public AccountTypeEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AccountTypeEntity valueOf(String name) {
        if (name == null) return  null;
        switch (name.toLowerCase().trim()) {
            case CHECKING:
                return new AccountTypeEntity(1, CHECKING);
            case SAVINGS:
                return new AccountTypeEntity(2, SAVINGS);
            default:
                return null;
        }
    }

    @Id
    private int id;

    @Column(name="name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
