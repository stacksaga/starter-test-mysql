package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.ReserveOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PlaceOrderController {

    @Autowired
    private SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/test")
    public ResponseEntity<?> placeOrder() {
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUpdatedStatus("INIT_STEP>");
        orderAggregator.setType(OrderAggregator.Type.revert_error);
        TransactionResponse<OrderAggregator> response = orderAggregatorSagaTemplate.doProcess(
                orderAggregator,
                ReserveOrder.class
        );
        return ResponseEntity.ok().build();
    }


}

