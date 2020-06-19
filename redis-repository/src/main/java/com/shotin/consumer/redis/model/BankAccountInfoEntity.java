package com.shotin.consumer.redis.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.shotin.consumer.redis.model.BankAccountInfoEntity.BANK_ACCOUNT_INFO_KEY;

@Data
@RedisHash(BANK_ACCOUNT_INFO_KEY)
@NoArgsConstructor
public class BankAccountInfoEntity {

    public static final String BANK_ACCOUNT_INFO_KEY = "bank-account-info";

    @Id
    private UUID uuid;

    private BankAccountEntity bankAccount;

    private AddressEntity address;

    public BankAccountInfoEntity(UUID uuid, BankAccountEntity bankAccount, AddressEntity addressEntity) {
        this.uuid = uuid;
        this.bankAccount = bankAccount;
        this.address = addressEntity;
    }

    public BankAccountInfoEntity(Map<Object, Object> map) {
        if (map == null || map.isEmpty()) throw new IllegalArgumentException("The input Map should not be null");
        bankAccount = new BankAccountEntity();
        address = new AddressEntity();
        for(Map.Entry<Object, Object> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            switch(key) {
                case "uuid":
                    uuid = UUID.fromString(String.valueOf(map.get("uuid")));
                    break;
                case "bankAccount.uuid":
                    bankAccount.setUuid(UUID.fromString(String.valueOf(entry.getValue())));
                    break;
                case "bankAccount.firstName":
                    bankAccount.setFirstName(String.valueOf(entry.getValue()));
                    break;
                case "bankAccount.lastName":
                    bankAccount.setLastName(String.valueOf(entry.getValue()));
                    break;
                case "bankAccount.patronymic":
                    bankAccount.setPatronymic(String.valueOf(entry.getValue()));
                    break;
                case "bankAccount.accountNumber":
                    bankAccount.setAccountNumber(Long.parseLong(String.valueOf(entry.getValue())));
                    break;
                case "bankAccount.accountType":
                    bankAccount.setAccountType(AccountType.valueOf(String.valueOf(entry.getValue())));
                    break;
                case "address.street":
                    address.setStreet(String.valueOf(entry.getValue()));
                    break;
                case "address.city":
                    address.setCity(String.valueOf(entry.getValue()));
                    break;
                case "address.state":
                    address.setState(String.valueOf(entry.getValue()));
                    break;
            }
        }

    }

    public Map<String, String> asMap() {
        Map<String, String> map = new HashMap<>();
        putIfNotNull(map, "uuid", uuid);
        if (bankAccount != null) {
            putIfNotNull(map, "_class", this.getClass().getName());
            putIfNotNull(map, "bankAccount.uuid", bankAccount.getUuid());
            putIfNotNull(map, "bankAccount.lastName", bankAccount.getLastName());
            putIfNotNull(map, "bankAccount.firstName", bankAccount.getFirstName());
            putIfNotNull(map, "bankAccount.patronymic", bankAccount.getPatronymic());
            putIfNotNull(map, "bankAccount.accountNumber", bankAccount.getAccountNumber());
            putIfNotNull(map, "bankAccount.accountType", bankAccount.getAccountType());
        }
        if (address != null) {
            putIfNotNull(map, "address.street", address.getStreet());
            putIfNotNull(map, "address.city", address.getCity());
            putIfNotNull(map, "address.state", address.getState());
        }
        return map;
    }

    private void putIfNotNull(Map<String, String> map, String key, Object value) {
        if (value != null) {
            map.put(key, String.valueOf(value));
        }
    }
}
