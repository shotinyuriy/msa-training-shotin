package com.shotin.grpc.config;

import com.shotin.grpc.server.HelloServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class GrpcConfig {

    @PostConstruct
    public void startServer() {
        HelloServer helloServer = helloServer();
        try {
            helloServer.start();
            helloServer.blockUntilShutdown();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public HelloServer helloServer() {
        HelloServer helloServer = new HelloServer(9009);
        return helloServer;
    }
}
