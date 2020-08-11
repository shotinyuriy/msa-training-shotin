package com.shotin.blacklist.loader.oracle.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name="bank_account_info")
@Getter
@Setter
@NoArgsConstructor
public class BankAccountInfoEntity {

    @Id
    private UUID uuid;

    @Column(name="update_id")
    private Long updateId;

    @Column(name="last_name")
    private String lastName;

    @Column(name="first_name")
    private String firstName;

    private String patronymic;

    private String city;

    @Column(name = "black_listed")
    private Boolean blackListed;
}
