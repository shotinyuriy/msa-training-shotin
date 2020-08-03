package com.shotin.grpc.config;

import com.shotin.grpc.server.BankAccountInfoServer;
import com.shotin.grpc.service.BankAccountInfoService;
import com.shotin.grpc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class GrpcConfig {

    @Autowired
    HelloService helloService;

    @Autowired
    BankAccountInfoService bankAccountInfoService;

    @PostConstruct
    public void startServer() {
        BankAccountInfoServer helloServer = helloServer();
        try {
            helloServer.start();
            helloServer.blockUntilShutdown();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public BankAccountInfoServer helloServer() {
        BankAccountInfoServer helloServer = new BankAccountInfoServer(9009, bankAccountInfoService, helloService);
        return helloServer;
    }
}
