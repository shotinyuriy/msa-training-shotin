package com.shotin.grpc.client;

import com.shotin.grpc.*;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Future;

public class HelloClient {
    private static final Logger LOG = LoggerFactory.getLogger(HelloClient.class);

    private final HelloServiceGrpc.HelloServiceBlockingStub blockingStub;
    private final HelloServiceGrpc.HelloServiceStub asyncStub;
    private final BankAccountInfoServiceGrpc.BankAccountInfoServiceBlockingStub bankAccountInfoBlockingStub;

    public HelloClient(String host, int port) {
        this(ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext());
    }

    public HelloClient(ManagedChannelBuilder<?> channelBuilder) {
        Channel channel = channelBuilder.build();
        blockingStub = HelloServiceGrpc.newBlockingStub(channel);
        asyncStub = HelloServiceGrpc.newStub(channel);
        bankAccountInfoBlockingStub = BankAccountInfoServiceGrpc.newBlockingStub(channel);
    }

    public String hello(String firstName, String lastName) {
        HelloRequest helloRequest = HelloRequest.newBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();

        return blockingStub.hello(helloRequest).getGreeting();
    }

    public void helloAsync(String firstName, String lastName, StreamObserver<HelloResponse> responseObserver) {
        HelloRequest helloRequest = HelloRequest.newBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();
        asyncStub.hello(helloRequest, responseObserver);
    }

    public BankAccountInfo findByUuid(String uuid) {
        UuidRequest uuidRequest = UuidRequest.newBuilder().setUuid(uuid).build();
        return bankAccountInfoBlockingStub.findByUuid(uuidRequest);
    }
}
