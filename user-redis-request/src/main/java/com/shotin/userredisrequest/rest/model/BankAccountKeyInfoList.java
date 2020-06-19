package com.shotin.userredisrequest.rest.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class BankAccountKeyInfoList {
    private final List<String> keys;
}
