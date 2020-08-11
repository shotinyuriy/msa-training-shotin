package com.shotin.grpc.service;

import com.shotin.grpc.*;
import com.shotin.grpc.model.BankAccountInfoEntity;
import com.shotin.grpc.repository.BankAccountInfoRepository;
import groovy.util.logging.Slf4j;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountInfoService extends BankAccountInfoServiceGrpc.BankAccountInfoServiceImplBase {

    private final BankAccountInfoRepository bankAccountInfoRepository;

    @Override
    public void findByUuid(UuidRequest request, StreamObserver<BankAccountInfo> responseObserver) {

        byUuid(request.getUuid())
                .subscribe(new Subscriber<BankAccountInfo>() {
                    Subscription sub;
                    @Override
                    public void onSubscribe(Subscription s) {
                        sub = s;
                        s.request(1);
                    }

                    @Override
                    public void onNext(BankAccountInfo bankAccountInfo) {
                        responseObserver.onNext(bankAccountInfo);
                    }

                    @Override
                    public void onError(Throwable t) {
                        responseObserver.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        responseObserver.onCompleted();
                    }
                });
    }

    @Override
    public void findByAccountType(BankAccountRequest request, StreamObserver<BankAccountResponse> responseObserver) {
        super.findByAccountType(request, responseObserver);
    }

    private Mono<BankAccountInfo> byUuid(String uuid) {
        Mono<BankAccountInfoEntity> mono = bankAccountInfoRepository
                .findById(UUID.fromString(uuid));

        Mono<BankAccountInfo> mappedMono = mono.map(bae -> {
                    BankAccountInfo bankAccountInfo = BankAccountInfo.newBuilder()
                            .setBankAccount(BankAccount.newBuilder()
                                    .setLastName(bae.getBankAccount().getLastName()))
                            .setAddress(Address.newBuilder()
                                    .setStreet(bae.getAddress().getStreet()))
                            .build();
                            return  bankAccountInfo;
                });
        return mappedMono;
    }
}
