package com.shotin.grpc.stream;

import io.grpc.stub.StreamObserver;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.BaseSubscriber;

public class ResponseObserver<T> implements StreamObserver<T>, Publisher<T> {

    private BaseSubscriber<T> subscriber = new BaseSubscriber<T>() {
    };

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void subscribe(Subscriber s) {

    }
}
