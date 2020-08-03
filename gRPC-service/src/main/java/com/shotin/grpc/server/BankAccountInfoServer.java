package com.shotin.grpc.server;

import com.shotin.grpc.service.BankAccountInfoService;
import com.shotin.grpc.service.HelloService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class BankAccountInfoServer {

    private static final Logger logger = Logger.getLogger(BankAccountInfoServer.class.getName());

    private final int port;
    private final BankAccountInfoService bankAccountInfoService;
    private final HelloService helloService;
    private Server server;

//    public BankAccountInfoServer(int port) {
//        this(ServerBuilder.forPort(port), port);
//    }

//    public BankAccountInfoServer(ServerBuilder<?> serverBuilder, int port) {
//        this.port = port;
//        this.server = serverBuilder.addService(new HelloService()).build();
//    }

    @PostConstruct
    public void init() {
        ServerBuilder serverBuilder = ServerBuilder.forPort(port);
        this.server = serverBuilder
                .addService(helloService)
                .addService(bankAccountInfoService)
                .build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    BankAccountInfoServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println("*** server shut down");
            }
        });
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
