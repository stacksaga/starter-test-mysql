package com.example.controller;

import com.example.aggregator.AddCustomerAggregator;
import com.example.executors.ReserveOrder;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.core.lsitener.AggregatorListener;
import org.mono.stacksaga.exception.EventStoreConnectionException;
import org.mono.stacksaga.exception.execution.RevertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerAddController implements AggregatorListener<AddCustomerAggregator> {
    @Autowired
    private SagaTemplate<AddCustomerAggregator> addCustomerAggregatorSagaTemplate;

    @GetMapping("/test2")
    public ResponseEntity<?> placeOrder() throws EventStoreConnectionException, RevertException {
        AddCustomerAggregator addCustomerAggregator = new AddCustomerAggregator();
        TransactionResponse<AddCustomerAggregator> response = addCustomerAggregatorSagaTemplate.process(
                addCustomerAggregator,
                ReserveOrder.class
        );
        return ResponseEntity.ok().build();
    }

}
