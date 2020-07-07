package com.shotin.bankaccountgenerator.rest;

import com.shotin.bankaccountgenerator.generator.BankAccountGenerator;
import com.shotin.bankaccountgenerator.model.BankAccount;
import com.shotin.bankaccountgenerator.model.BankAccountList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bankAccounts")
public class BankAccountRestResource {

    public static final String CHARSET = "utf-8";

    private BankAccountGenerator bankAccountGenerator;

    public BankAccountRestResource(@Autowired BankAccountGenerator bankAccountGenerator) {
        this.bankAccountGenerator = bankAccountGenerator;
    }

    @Operation(summary = "Generate a single random bank account")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Generated a bank account",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=" + CHARSET,
                            schema = @Schema(implementation = BankAccount.class)))
    })
    @GetMapping(path = "/random", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankAccount> getRandomBankAccount() {
        BankAccount bankAccount = bankAccountGenerator.generateBankAccount();

        ResponseEntity<BankAccount> responseEntity = ResponseEntity.ok(bankAccount);
        return responseEntity;
    }

    @Operation(summary = "Generate many random bank accounts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Generated many bank accounts",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE + ";charset=" + CHARSET,
                            schema = @Schema(implementation = BankAccount.class)))
    })
    @GetMapping(path = "/manyRandom/{count}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRandomBankAccount(@PathVariable int count) {
        if (count < 1) {
            return ResponseEntity.badRequest().body("count should be an integer greater than zero");
        }
        List<BankAccount> bankAccounts = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            BankAccount bankAccount = bankAccountGenerator.generateBankAccount();
            bankAccounts.add(bankAccount);
        }

        BankAccountList bankAccountList = new BankAccountList();
        bankAccountList.setBankAccounts(bankAccounts);

        ResponseEntity<Object> responseEntity = ResponseEntity.ok(bankAccountList);
        return responseEntity;
    }
}
