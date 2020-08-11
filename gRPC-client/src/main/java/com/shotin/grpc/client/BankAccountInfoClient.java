package com.shotin.grpc.client;

import com.shotin.grpc.BankAccount;
import com.shotin.grpc.BankAccountInfo;
import com.shotin.grpc.BankAccountInfoServiceGrpc;
import com.shotin.grpc.UuidRequest;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.ietf.jgss.ChannelBinding;

public class BankAccountInfoClient {

    private final BankAccountInfoServiceGrpc.BankAccountInfoServiceBlockingStub bankAccountInfoBlockingStub;

    public BankAccountInfoClient(String host, int port) {
        this(ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext());
    }

    public BankAccountInfoClient(ManagedChannelBuilder<?> channelBuilder) {
        Channel channel = channelBuilder.build();
        bankAccountInfoBlockingStub = BankAccountInfoServiceGrpc.newBlockingStub(channel);
    }

    public BankAccountInfo findByUuid(String uuid) {
        UuidRequest uuidRequest = UuidRequest.newBuilder().setUuid(uuid).build();
        return bankAccountInfoBlockingStub.findByUuid(uuidRequest);
    }
}
