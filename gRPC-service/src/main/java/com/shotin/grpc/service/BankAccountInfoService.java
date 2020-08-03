package com.shotin.grpc.service;

import com.shotin.grpc.*;
import com.shotin.grpc.repository.BankAccountInfoRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountInfoService extends BankAccountInfoServiceGrpc.BankAccountInfoServiceImplBase {

    private final BankAccountInfoRepository bankAccountInfoRepository;

    @Override
    public void findByUuid(UuidRequest request, StreamObserver<BankAccountInfo> responseObserver) {
        responseObserver.onNext(byUuid(request.getUuid()));
        responseObserver.onCompleted();
    }

    @Override
    public void findByAccountType(BankAccountRequest request, StreamObserver<BankAccountResponse> responseObserver) {
        super.findByAccountType(request, responseObserver);
    }

    private BankAccountInfo byUuid(String uuid) {
        return bankAccountInfoRepository.findById(UUID.fromString(uuid))
                .map(bae -> {
                    BankAccountInfo bankAccountInfo = BankAccountInfo.newBuilder()
                            .setBankAccount(BankAccount.newBuilder()
                                    .setLastName(bae.getBankAccount().getLastName()))
                            .setAddress(Address.newBuilder()
                                    .setStreet(bae.getAddress().getStreet()))
                            .build();
                            return  bankAccountInfo;
                }).block();

    }
}
