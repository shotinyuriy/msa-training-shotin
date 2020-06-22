package com.shotin.userredisrequestsync.rest.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class BankAccountInfoKeys {
    private final List<String> keys;
}
