package com.shotin.grpc.service;

import com.shotin.grpc.BankAccountInfoServiceGrpc;
import com.shotin.grpc.BankAccountRequest;
import com.shotin.grpc.BankAccountResponse;
import com.shotin.grpc.repository.BankAccountInfoRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountInfoService extends BankAccountInfoServiceGrpc.BankAccountInfoServiceImplBase {

    private final BankAccountInfoRepository bankAccountInfoRepository;

    @Override
    public void findByAccountType(BankAccountRequest request, StreamObserver<BankAccountResponse> responseObserver) {
        super.findByAccountType(request, responseObserver);
    }
}
