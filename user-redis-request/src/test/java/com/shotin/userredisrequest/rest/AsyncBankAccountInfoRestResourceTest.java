package com.shotin.userredisrequest.rest;

import com.shotin.consumer.redis.model.BankAccountInfoEntity;
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
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
public class AsyncBankAccountInfoRestResourceTest {

    @InjectMocks
    private AsyncBankAccountInfoRestResource restResource;

    @Mock
    private BankAccountInfoService bankAccountInfoService;

    @Test
    public void testBankAccountIfoKeys_ServiceException() throws Exception {
        Mockito.when(bankAccountInfoService.findAllKeys())
                .thenReturn(Flux.error(new RuntimeException("Redis Connection Failure")));
        CompletableFuture<ResponseEntity<BankAccountInfoKeys>> future = restResource.getAllKeys();
        ResponseEntity<BankAccountInfoKeys> responseEntity = future.get(1000, TimeUnit.MILLISECONDS);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        Assertions.assertNull(responseEntity.getBody(), "response body");
    }

    @Test
    public void testBankAccountInfoKeys_Success() throws Exception {
        Mockito.when(bankAccountInfoService.findAllKeys())
                .thenReturn(Flux.just("uuid1", "uuid2", "uuid3"));

        CompletableFuture<ResponseEntity<BankAccountInfoKeys>> future = restResource.getAllKeys();
        ResponseEntity<BankAccountInfoKeys> responseEntity = future.get(1000, TimeUnit.MILLISECONDS);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "response status");
        Assertions.assertNotNull(responseEntity.getBody(), "response body");
        Assertions.assertNotNull(responseEntity.getBody().getKeys(), "response body keys");
        Assertions.assertEquals( 3, responseEntity.getBody().getKeys().size(), "response body keys size");
    }

    @Test
    public void testGetBankAccountInfoById_ServiceException() throws Exception {
        Mockito.when(bankAccountInfoService.findById(Mockito.anyString()))
                .thenReturn(Mono.error(new RuntimeException("Redis Connection Failure")));

        CompletableFuture<ResponseEntity<BankAccountInfoEntity>> future = restResource.getBankAccountInfoById(UUID.randomUUID().toString());
        ResponseEntity<BankAccountInfoEntity> responseEntity = future.get(1000, TimeUnit.MILLISECONDS);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode(), "response status");
        Assertions.assertNull(responseEntity.getBody(), "response body");
    }

    @Test
    public void testGetBankAccountInfoById_Success() throws Exception {
        Mockito.when(bankAccountInfoService.findById(Mockito.anyString()))
                .thenReturn(Mono.just(new BankAccountInfoEntity()));

        CompletableFuture<ResponseEntity<BankAccountInfoEntity>> future = restResource.getBankAccountInfoById(UUID.randomUUID().toString());
        ResponseEntity<BankAccountInfoEntity> responseEntity = future.get(1000, TimeUnit.MILLISECONDS);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "response status");
        Assertions.assertNotNull(responseEntity.getBody(), "response body");
    }
}
