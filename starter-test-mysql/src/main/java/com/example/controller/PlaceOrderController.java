package com.example.controller;

import com.example.aggregator.OrderAggregator;
import com.example.executors.CheckUserExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mono.stacksaga.SagaTemplate;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.core.lsitener.AggregatorEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceOrderController implements AggregatorEventListener<OrderAggregator> {

    @Autowired
    private SagaTemplate<OrderAggregator> orderAggregatorSagaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("")
    public void placeOrder() {

        /*System.out.println("orderAggregatorSagaTemplate.hashCode() = " + orderAggregatorSagaTemplate.hashCode());
        OrderAggregator orderAggregator = new OrderAggregator();
        orderAggregator.setUsername("mafei");
        orderAggregator.setAmount(49999.0);
        OrderAggregator.Tmp tmp = new OrderAggregator.Tmp();
        tmp.setUsername("fuck");
        orderAggregator.setTmp(tmp);
        orderAggregatorSagaTemplate.doProcess(
                orderAggregator,
                CheckUserExecutor.class,
                objectMapper
        );*/
    }

    @Override
    public void onTransactionSuccess(TransactionResponse<OrderAggregator> response) {
        System.out.println("PlaceOrderController.onTransactionSuccess");
    }
}

