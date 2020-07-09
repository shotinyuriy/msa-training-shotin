package com.shotin.userredisrequest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;

@Configuration
public class ReactiveConfig {

    @Bean
    public Scheduler reactiveScheduler() {
        return Schedulers.elastic();
    }
}
