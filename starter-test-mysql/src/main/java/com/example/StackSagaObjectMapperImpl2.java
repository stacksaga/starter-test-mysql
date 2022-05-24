package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mono.stacksaga.core.StackSagaObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StackSagaObjectMapperImpl2 {

    @Bean
    public StackSagaObjectMapper getStackSagaObjectMap(){
        System.out.println("StackSagaObjectMapperImpl2.getStackSagaObjectMap");
        return ObjectMapper::new;
    }
}
