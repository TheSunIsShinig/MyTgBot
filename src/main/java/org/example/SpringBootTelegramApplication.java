package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootTelegramApplication {

    public static void main(String[] args) {
        System.out.println("Starting Spring Boot application...");
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringBootTelegramApplication.class, args);
        System.out.println("Starting Spring Boot application...");
    }
}
