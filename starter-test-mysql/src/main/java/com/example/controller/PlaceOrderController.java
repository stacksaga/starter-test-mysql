package com.example.controller;

import com.example.aggregator.OrderAggregator;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.core.lsitener.AggregatorEventListener;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceOrderController implements AggregatorEventListener<OrderAggregator>, TestM<String>, TestN {
    @Override
    public void onTransactionSuccess(TransactionResponse<OrderAggregator> response) {
        System.out.println("PlaceOrderController.onTransactionSuccess");
    }
}

interface TestM<T> {

}

interface TestN {

}

