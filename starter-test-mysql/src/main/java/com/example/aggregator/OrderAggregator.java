package com.example.aggregator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.SagaAggregate;
import org.mono.stacksaga.core.AggregatorMapper;
import org.mono.stacksaga.core.SagaAggregatorMapper;
import org.mono.stacksaga.core.annotation.Aggregator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Slf4j
@Aggregator(version = "1.0.6", description = "aggregator test description", mapper = OrderAggregator.Mapper.class, versionUpdateNote = "order aggregator update note for 1.0.5")
public class OrderAggregator extends SagaAggregate {
    private String updatedStatus;
    private Date time;
    private Type type;


    @AllArgsConstructor
    @Component
    public static class Mapper implements AggregatorMapper {

        @Override
        public SagaAggregatorMapper setSagaAggregatorMapper() {
            log.debug("invoked SagaAggregatorMapper");
            return SagaAggregatorMapper.Builder.build(new ObjectMapper());
        }
    }

    public enum Type {
        revert_complete,
        process_complete,
        revert_error,
    }

}
