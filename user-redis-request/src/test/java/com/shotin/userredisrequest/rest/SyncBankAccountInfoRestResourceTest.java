package com.shotin.userredisrequest.rest;

import com.shotin.userredisrequest.rest.model.BankAccountInfoKeys;
import com.shotin.userredisrequest.service.BankAccountInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)
public class SyncBankAccountInfoRestResourceTest {

    @InjectMocks
    private SyncBankAccountInfoRestResource restResource;

    @Mock
    private BankAccountInfoService bankAccountInfoService;

    @Test
    public void testBankAccountInfoKeys_ServiceException() throws ExecutionException, InterruptedException {
        Mockito.when(bankAccountInfoService.findAllKeys())
                .thenReturn(Flux.error(new RuntimeException("Redis Connection Failure")));

        ResponseEntity<BankAccountInfoKeys> responseEntity = restResource.bankAccountInfoKeys();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode(), "response status");
        Assertions.assertNull(responseEntity.getBody(), "response body");
    }

    @Test
    public void testBankAccountInfoKeys_Success() throws ExecutionException, InterruptedException {
        Mockito.when(bankAccountInfoService.findAllKeys())
                .thenReturn(Flux.just("uuid1", "uuid2", "uuid"));

        ResponseEntity<BankAccountInfoKeys> responseEntity = restResource.bankAccountInfoKeys();

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "response status");
        Assertions.assertNotNull(responseEntity.getBody(), "response body");
        Assertions.assertEquals(3, responseEntity.getBody().getKeys().size(), "response body keys size");
    }

    @Test
    public void testGetBankAccountInfoById_ServiceException() {

    }
}
