package com.example;

import org.mono.stacksaga.EnableStackSaga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mono.stacksaga.common.Resources.Redis.Templates.STACK_SAGA_REDIS_TEMPLATE_STRING_STRING;

@SpringBootApplication
@EnableStackSaga
public class StarterTestMysqlApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StarterTestMysqlApplication.class, args);
    }

    @Autowired
    @Qualifier(STACK_SAGA_REDIS_TEMPLATE_STRING_STRING)
    private RedisTemplate<String, String> redisTemplate;



    @Override
    public void run(String... args) throws Exception {
//        Optional<AggregatorEntity> test = aggregatorService.getBy("test");
//        if (test.isPresent()) {
//            System.out.println("test = " + test);
//        }
//        Connection connection = getConnection();

    }
}
