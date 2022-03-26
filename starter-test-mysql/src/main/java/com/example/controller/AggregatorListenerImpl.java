package com.example.controller;

import com.example.aggregator.OrderAggregator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mono.stacksaga.TransactionResponse;
import org.mono.stacksaga.core.annotation.AsyncListener;
import org.mono.stacksaga.core.lsitener.AggregatorListener;
import org.mono.stacksaga.executor.utils.ProcessStack;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AggregatorListenerImpl implements AggregatorListener<OrderAggregator> {
    @SneakyThrows
    @Override
    @AsyncListener
    public void onEachProcessPerformed(ProcessStack<OrderAggregator> previousProcessStack, OrderAggregator currentAggregate) {
        log.info("PlaceOrderController.onProcessPerformed");
    }

    @Override
    @AsyncListener
    public void onTransactionSuccess(TransactionResponse<OrderAggregator> response) {
        System.out.println("PlaceOrderController.onTransactionSuccess");
    }

}
