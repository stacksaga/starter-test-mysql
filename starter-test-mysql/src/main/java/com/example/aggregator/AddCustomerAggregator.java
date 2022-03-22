package com.example.aggregator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.SagaAggregate;
import org.mono.stacksaga.core.AggregatorMapper;
import org.mono.stacksaga.core.SagaAggregatorMapper;
import org.mono.stacksaga.core.annotation.Aggregator;
import org.springframework.stereotype.Component;

@Aggregator(version = "1.0.2", mapper = AddCustomerAggregator.Mapper.class)
@Slf4j
public class AddCustomerAggregator extends SagaAggregate {

    @AllArgsConstructor
    @Component
    public static class Mapper implements AggregatorMapper {

        @Override
        public SagaAggregatorMapper setSagaAggregatorMapper() {
            log.debug("invoked SagaAggregatorMapper");
            return SagaAggregatorMapper.Builder.build(new ObjectMapper());
        }
    }

}
