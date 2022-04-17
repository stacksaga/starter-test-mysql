package com.example;

import org.mono.stacksaga.EnableStackSaga;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
//        LiquibaseAutoConfiguration.class
})
@EnableStackSaga
@EntityScan({
        "com.example.ms.userservice.entity",
        "com.example.ms.orderservice.entity",
        "com.example.ms.customerwalletservice.entity",
})
@EnableJpaRepositories({
        "com.example.ms.userservice.repository",
        "com.example.ms.orderservice.repository",
        "com.example.ms.customerwalletservice.repository",
})
public class StackSagaLoadTester {

    public static void main(String[] args) {
        SpringApplication.run(StackSagaLoadTester.class, args);
    }
}
