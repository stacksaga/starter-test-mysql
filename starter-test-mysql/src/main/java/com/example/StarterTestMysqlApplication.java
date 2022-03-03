package com.example;

import org.mono.stacksaga.EnableStackSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mono.stacksaga.common.Resources.Redis.Templates.STACK_SAGA_REDIS_TEMPLATE_STRING_STRING;

@SpringBootApplication
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
public class StarterTestMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarterTestMysqlApplication.class, args);
    }
}
