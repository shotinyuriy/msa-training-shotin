package com.shotin.bankaccountgenerator.config;

import com.shotin.bankaccountgenerator.generator.FromFileNamesGenerator;
import com.shotin.bankaccountgenerator.generator.NamesGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneratorConfig {

    @Bean
    @ConfigurationProperties(prefix = "generator.man")
    public NamesGenerator manNamesGenerator() {
        return new FromFileNamesGenerator();
    }

    @Bean
    @ConfigurationProperties(prefix = "generator.woman")
    public NamesGenerator womanNamesGenerator() {
        return new FromFileNamesGenerator();
    }
}
