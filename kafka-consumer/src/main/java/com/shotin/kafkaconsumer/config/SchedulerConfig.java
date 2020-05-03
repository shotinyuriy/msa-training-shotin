package com.shotin.kafkaconsumer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("!test")
@EnableScheduling
@Configuration
public class SchedulerConfig {
}
