package com.shotin.grpc;

import com.shotin.grpc.server.HelloServer;

import java.io.IOException;

public class App {

    public String getStarted() {
        return "STARTED gRPC-SERVER";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(new App().getStarted());

        HelloServer helloServer = new HelloServer(9009);
        helloServer.start();
        helloServer.blockUntilShutdown();
    }
}
