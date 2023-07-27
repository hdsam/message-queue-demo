package com.hdsam.message.spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * RabbitAmqpTutorialsApplication
 *
 * @author Yeo
 * @date 2023/7/21
 */
@SpringBootApplication
@EnableScheduling
public class RabbitAmqpTutorialsApplication {

    @Profile("usage_message")
    @Bean
    public CommandLineRunner usage() {
        return args -> usageMessage();
    }

    void usageMessage() {
        System.out.println("This app uses Spring Profiles to control its behavior.");
        System.out.println("Sample usage: java -jar rabbitmq-hello-world-spring.jar " +
                "--spring.profiles.active={hello-world},sender");
    }

    @Profile("!usage_message")
    @Bean
    public CommandLineRunner tutorial() {
        return new RabbitAmqpTutorialsRunner();
    }

    public static void main(String[] args) {
        SpringApplication.run(RabbitAmqpTutorialsApplication.class, args);
    }
}
