package com.shotin.grpc;

import com.shotin.grpc.repository.MockBankAccountInfoRepository;
import com.shotin.grpc.server.BankAccountInfoServer;
import com.shotin.grpc.service.BankAccountInfoService;
import com.shotin.grpc.service.HelloService;

import java.io.IOException;

public class App {

    public String getStarted() {
        return "STARTED gRPC-SERVER";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(new App().getStarted());


        HelloService helloService = new HelloService();

        BankAccountInfoService bankAccountInfoService = new BankAccountInfoService(new MockBankAccountInfoRepository());
        BankAccountInfoServer helloServer = new BankAccountInfoServer(9009, bankAccountInfoService, helloService);
        helloServer.init();
        helloServer.start();
        helloServer.blockUntilShutdown();
    }
}
