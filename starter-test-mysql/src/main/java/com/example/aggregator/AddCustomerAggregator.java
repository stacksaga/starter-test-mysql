package com.example.aggregator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.mono.stacksaga.SagaAggregate;
import org.mono.stacksaga.core.AggregatorMapper;
import org.mono.stacksaga.core.SagaAggregatorMapper;
import org.mono.stacksaga.core.annotation.Aggregator;

@Aggregator(version = "1.0.2", mapper = AddCustomerAggregator.Mapper.class)
public class AddCustomerAggregator extends SagaAggregate {

    @AllArgsConstructor
    public static class Mapper implements AggregatorMapper {

        @Override
        public SagaAggregatorMapper setSagaAggregatorMapper() {
            return SagaAggregatorMapper.Builder.build(new ObjectMapper());
        }
    }

}
