package com.example.aggregator;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mono.stacksaga.SagaAggregate;
import org.mono.stacksaga.core.annotation.Aggregator;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Aggregator(version = "1.0.3", versionUpdateNote = "just for fun")
@Data
@ToString
public class OrderAggregator extends SagaAggregate {
    private String username;
    private Double amount;
    private Tmp tmp;

    @Data
    @ToString
    @NoArgsConstructor
    public static class Tmp implements Serializable {
        private String username;
    }
}
