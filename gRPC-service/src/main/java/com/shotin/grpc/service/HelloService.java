package com.shotin.grpc.service;

import com.shotin.grpc.HelloRequest;
import com.shotin.grpc.HelloResponse;
import com.shotin.grpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

public class HelloService extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(HelloRequest helloRequest, StreamObserver<HelloResponse> responseObserver) {
        responseObserver.onNext(greet(helloRequest));
        responseObserver.onCompleted();
    }

    private HelloResponse greet(HelloRequest request) {

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting("Hello, " + request.getFirstName() + " " + request.getLastName() + "!")
                .build();

        return response;
    }
}
