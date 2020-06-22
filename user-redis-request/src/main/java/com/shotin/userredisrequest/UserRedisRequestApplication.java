package com.shotin.userredisrequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class UserRedisRequestApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserRedisRequestApplication.class, args);
	}

}
