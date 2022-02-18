package com.example.aggregator;

import org.mono.stacksaga.SagaAggregate;
import org.mono.stacksaga.core.annotation.Aggregator;

@Aggregator(version = "1.0.3",versionUpdateNote = "just for fun")
public class OrderAggregator extends SagaAggregate {
}
