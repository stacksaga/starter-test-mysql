package com.example.controller;

import com.example.aggregator.AddCustomerAggregator;
import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.core.lsitener.AggregatorListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerAddController implements AggregatorListener<AddCustomerAggregator> {
    @Autowired
    private SagaTemplate<AddCustomerAggregator> addCustomerAggregatorSagaTemplate;

    @GetMapping("/test2")
    public ResponseEntity<?> placeOrder() {
        AddCustomerAggregator addCustomerAggregator = new AddCustomerAggregator();
        TransactionResponse<AddCustomerAggregator> response = addCustomerAggregatorSagaTemplate.doProcess(
                addCustomerAggregator,
                ReserveOrder.class
        );
        return ResponseEntity.ok().build();
    }

}
