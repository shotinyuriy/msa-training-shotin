package com.shotin.userredisrequest.rest;

import com.shotin.userredisrequest.rest.model.BankAccountInfoKeys;
import com.shotin.userredisrequest.service.BankAccountInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class BankAccountInfoRestResourceTest {

    @InjectMocks
    private BankAccountInfoRestResource restResource;

    @Mock
    private BankAccountInfoService bankAccountInfoService;

    @Test
    public void testBankAccountInfoKeys_ServiceException() {
        Mockito.when(bankAccountInfoService.findAllKeys())
                .thenReturn(Flux.error(new RuntimeException("Redis Connection Failure")));

        Mono<BankAccountInfoKeys> response = restResource.bankAccountInfoKeys();


    }
}
